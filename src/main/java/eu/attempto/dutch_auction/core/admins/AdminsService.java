package eu.attempto.dutch_auction.core.admins;

import eu.attempto.dutch_auction.auth.AuthService;
import eu.attempto.dutch_auction.auth.dto.RegistrationDto;
import eu.attempto.dutch_auction.core.auctions.AuctionsService;
import eu.attempto.dutch_auction.core.auctions.dto.ChangeAuctionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminsService {
  private final AuthService authService;
  private final AuctionsService auctionsService;

  public String createAdmin(RegistrationDto registrationDto) {
    authService.registration(registrationDto, true);

    return "OK";
  }

  public String changeAuction(ChangeAuctionDto changeAuctionDto) {
    return auctionsService.changeAuction(changeAuctionDto);
  }
}
