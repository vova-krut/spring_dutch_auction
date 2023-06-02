package eu.attempto.dutch_auction.auth;

import eu.attempto.dutch_auction.auth.dto.AuthResponse;
import eu.attempto.dutch_auction.auth.dto.LoginDto;
import eu.attempto.dutch_auction.auth.dto.RegistrationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Auth")
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/registration")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponse registration(@Valid @RequestBody RegistrationDto registrationDto) {
    return authService.registration(registrationDto, false);
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody LoginDto loginDto) {
    return authService.login(loginDto);
  }
}
