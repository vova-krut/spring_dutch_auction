package eu.attempto.dutch_auction.core.admins;

import eu.attempto.dutch_auction.auth.dto.RegistrationDto;
import eu.attempto.dutch_auction.core.auctions.dto.ChangeAuctionDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RolesAllowed("ADMIN")
@Tag(name = "Admins")
@RequestMapping("/admins")
public class AdminsController {
  private final AdminsService adminsService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String createAdmin(@Valid @RequestBody RegistrationDto registrationDto) {
    return adminsService.createAdmin(registrationDto);
  }

  @PatchMapping("/auctions")
  public String changeAuction(@Valid @RequestBody ChangeAuctionDto changeAuctionDto) {
    return adminsService.changeAuction(changeAuctionDto);
  }
}
