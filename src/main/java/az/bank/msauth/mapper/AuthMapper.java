package az.bank.msauth.mapper;

import az.bank.msauth.dao.AuthUserEntity;
import az.bank.msauth.model.SignUpDto;

public class AuthMapper {
    public static AuthUserEntity toEntity(SignUpDto dto){
        return AuthUserEntity.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }
}
