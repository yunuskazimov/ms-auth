package az.bank.msauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
 private String firstName;
 private String lastName;
 private String username;
 private String password;
 private String email;
}
