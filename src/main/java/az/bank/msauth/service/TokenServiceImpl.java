package az.bank.msauth.service;

import az.bank.msauth.dao.ActiveRefreshTokenEntity;
import az.bank.msauth.model.RefreshTokenDto;
import az.bank.msauth.model.exception.AuthException;
import az.bank.msauth.repository.ActiveRefreshTokenRepository;
import az.bank.msauth.util.DateUtil;
import az.bank.msauth.util.PayloadUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;

import static az.bank.msauth.model.constants.ErrorCodes.ACCESS_TOKEN_EXPIRED;
import static az.bank.msauth.model.constants.ErrorCodes.REFRESH_TOKEN_EXPIRED;
import static az.bank.msauth.util.PayloadUtil.extractField;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final RSAKey rsaJWK;
    private final ActiveRefreshTokenRepository activeRefreshTokenRepository;

    public TokenServiceImpl(ActiveRefreshTokenRepository activeRefreshTokenRepository) throws Exception {
        this.activeRefreshTokenRepository = activeRefreshTokenRepository;
        this.rsaJWK = new RSAKeyGenerator(2048)
                .keyID("123")
                .generate();
    }


    @Override
    public String generateAccessToken(Long userId) {
        try {
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .claim("userId", userId)
                    .claim("expirationTime", LocalDateTime.now().plusMinutes(10L).toString())
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                    claims.toPayload());

            JWSSigner signer = new RSASSASigner(rsaJWK);
            jwsObject.sign(signer);

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Something go wrong is jwt", e);
            throw new AuthException("auth.generate-jwt-exception", e.getMessage());
        }
    }

    @Override
    public String generateRefreshToken(RefreshTokenDto refreshTokenDto) {
        try {
            JWSSigner signer = new RSASSASigner(rsaJWK);

            ActiveRefreshTokenEntity activeRefreshTokenEntity =
                    activeRefreshTokenRepository.save(new ActiveRefreshTokenEntity());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .claim("uuid", activeRefreshTokenEntity.getUuid())
                    .claim("userId", refreshTokenDto.getUserId())
                    .claim("expirationTime", refreshTokenDto.getExpirationTime().toString())
                    .claim("count", refreshTokenDto.getCount())
                    .build();

            JWSObject jwsObject = new JWSObject(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                    claimsSet.toPayload());

            jwsObject.sign(signer);

            return jwsObject.serialize();
        } catch (Exception e) {
            log.error("Something go wrong in jwt", e);
            throw new AuthException("auth.generate-jwt-exception",
                    e.getMessage());
        }
    }

    @Override
    public Long validateAccessToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(rsaJWK.toPublicJWK());
            if (!jwsObject.verify(verifier)) {
                throw new AuthException("auth.invalid-token", "Token not valid");
            }
            if (DateUtil.toLocalDateTime(PayloadUtil.extractField(jwsObject, "expirationTime"))
                    .isBefore(LocalDateTime.now())) {
                throw new AuthException(ACCESS_TOKEN_EXPIRED, "Access Token expired");
            }
            return Long.parseLong(extractField(jwsObject, "userId"));
        } catch (ParseException | IllegalStateException | JOSEException e) {
            log.error("Something go wrong is jwt", e);
            throw new AuthException("auth.validate-jwt-exception", e.getMessage());
        }
    }

    @Override
    public RefreshTokenDto validateRefreshToken(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            JWSVerifier jwsVerifier = new RSASSAVerifier(rsaJWK.toPublicJWK());
            if (!jwsObject.verify(jwsVerifier)) {
                throw new AuthException("auth.invalid-token", "Token not valid");
            }

            ActiveRefreshTokenEntity activeRefreshTokenEntity =
                    activeRefreshTokenRepository.findByUuid(extractField(jwsObject, "uuid"))
                            .orElseThrow(() -> new AuthException(REFRESH_TOKEN_EXPIRED, "Refresh token expired"));

            RefreshTokenDto dto = getRefreshTokenDto(jwsObject);


            if (dto.getExpirationTime().isBefore(LocalDateTime.now()) || dto.getCount() <= 0) {
                throw new AuthException(REFRESH_TOKEN_EXPIRED, "Refresh Token expired");
            }

            activeRefreshTokenRepository.delete(activeRefreshTokenEntity);
            return dto;
        } catch (ParseException | JOSEException | IllegalStateException e) {
            log.error("Something go wrong is jwt", e);
            throw new AuthException("auth.validate-jwt-exception", e.getMessage());
        }

    }

    private RefreshTokenDto getRefreshTokenDto(JWSObject jwsObject) {
        return RefreshTokenDto.builder()
                .userId(Long.parseLong(extractField(jwsObject, "userId")))
                .expirationTime(DateUtil.toLocalDateTime(extractField(jwsObject, "expirationTime")))
                .count(Integer.parseInt(extractField(jwsObject, "count")) - 1)
                .build();
    }
}
