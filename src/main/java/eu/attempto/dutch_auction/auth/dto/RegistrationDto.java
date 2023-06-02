package eu.attempto.dutch_auction.auth.dto;

import eu.attempto.dutch_auction.core.users.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
  @NotBlank @Email private String email;

  @NotBlank
  @Length(min = 4)
  private String password;

  @NotBlank private String name;

  public User toEntity() {
    return User.builder().email(email).name(name).password(password).build();
  }
}
