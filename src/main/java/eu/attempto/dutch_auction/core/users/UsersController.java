package eu.attempto.dutch_auction.core.users;

import eu.attempto.dutch_auction.core.users.dto.BuyCoinsDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users")
@RequestMapping("/users")
public class UsersController {
  private final UsersService usersService;

  @GetMapping("/me")
  public User getMe(Authentication authentication) {
    return (User) authentication.getPrincipal();
  }

  @GetMapping
  public Page<User> getUsers(Pageable pageable) {
    return usersService.getUsers(pageable);
  }

  @DeleteMapping("/me")
  public String deleteMe(Authentication authentication) {
    return usersService.deleteMe(authentication);
  }

  @PostMapping("/coins")
  public String buyCoins(
      Authentication authentication, @Valid @RequestBody BuyCoinsDto buyCoinsDto) {
    return usersService.buyCoins(authentication, buyCoinsDto);
  }
}
