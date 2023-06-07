package eu.attempto.dutch_auction.core.users;

import eu.attempto.dutch_auction.core.auctions.Auction;
import eu.attempto.dutch_auction.core.auctions.AuctionsService;
import eu.attempto.dutch_auction.core.bids.Bid;
import eu.attempto.dutch_auction.core.bids.BidsService;
import eu.attempto.dutch_auction.core.roles.RolesEnum;
import eu.attempto.dutch_auction.core.roles.RolesService;
import eu.attempto.dutch_auction.core.users.dto.BuyCoinsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UsersService {
  private final UsersRepository usersRepository;
  private final RolesService rolesService;
  private final AuctionsService auctionsService;
  private final BidsService bidsService;

  public Optional<User> findUserById(Long id) {
    return usersRepository.findById(id);
  }

  public Optional<User> findByEmail(String email) {
    return usersRepository.findByEmail(email);
  }

  public User createUser(User user, boolean admin) {
    var role = rolesService.getRole(admin ? RolesEnum.ADMIN : RolesEnum.USER);

    user.setRole(role);
    return usersRepository.save(user);
  }

  public Page<User> getUsers(Pageable pageable) {
    return usersRepository.findAll(pageable);
  }

  public String deleteMe(Authentication authentication) {
    var user = (User) authentication.getPrincipal();
    CompletableFuture<Auction[]> future1 =
        CompletableFuture.supplyAsync(() -> auctionsService.deleteUserAuctions(user));
    CompletableFuture<Bid[]> future2 =
        CompletableFuture.supplyAsync(() -> bidsService.deleteUserBids(user));

    // Wait for all tasks to complete
    CompletableFuture.allOf(future1, future2).join();

    usersRepository.delete(user);

    return "OK";
  }

  public String buyCoins(Authentication authentication, BuyCoinsDto buyCoinsDto) {
    var user = (User) authentication.getPrincipal();
    user.setBalance(user.getBalance().add(buyCoinsDto.getEuro().multiply(new BigDecimal(2))));
    usersRepository.save(user);

    return "OK";
  }
}
