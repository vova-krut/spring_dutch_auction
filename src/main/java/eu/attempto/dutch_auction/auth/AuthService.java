package eu.attempto.dutch_auction.auth;

import eu.attempto.dutch_auction.auth.dto.AuthResponse;
import eu.attempto.dutch_auction.auth.dto.LoginDto;
import eu.attempto.dutch_auction.auth.dto.RegistrationDto;
import eu.attempto.dutch_auction.core.users.UsersService;
import eu.attempto.dutch_auction.exceptions.BadRequestException;
import eu.attempto.dutch_auction.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  @Value("${security.salt}")
  private String salt;

  private final UsersService usersService;
  private final JwtService jwtService;
  private final PasswordEncoder encoder;

  public AuthResponse registration(RegistrationDto registrationDto, boolean admin) {
    var candidate = usersService.findByEmail(registrationDto.getEmail());
    if (candidate.isPresent()) {
      throw new BadRequestException("User with this email already exists");
    }

    registrationDto.setPassword(encoder.encode(salt + registrationDto.getPassword()));
    var user = usersService.createUser(registrationDto.toEntity(), admin);

    var accessToken = jwtService.createToken(user.getId());
    return AuthResponse.builder().accessToken(accessToken).build();
  }

  public AuthResponse login(LoginDto loginDto) {
    var user =
        usersService
            .findByEmail(loginDto.getEmail())
            .orElseThrow(() -> new BadRequestException("Email or password is invalid"));

    var passwordsMatch = encoder.matches(salt + loginDto.getPassword(), user.getPassword());
    if (!passwordsMatch) {
      throw new BadRequestException("Email or password is invalid");
    }

    var accessToken = jwtService.createToken(user.getId());
    return AuthResponse.builder().accessToken(accessToken).build();
  }
}
