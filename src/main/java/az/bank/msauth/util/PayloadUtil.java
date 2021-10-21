package az.bank.msauth.util;

import com.nimbusds.jose.JWSObject;

public class PayloadUtil {
    public static String extractField(JWSObject jwsObject, String fieldName) {
        return jwsObject.getPayload().toJSONObject().get(fieldName).toString();
    }
}
