package eu.attempto.dutch_auction.core.bids;

import eu.attempto.dutch_auction.core.auctions.Auction;
import eu.attempto.dutch_auction.core.auctions.AuctionsService;
import eu.attempto.dutch_auction.core.bids.dto.PlaceBidDto;
import eu.attempto.dutch_auction.core.users.User;
import eu.attempto.dutch_auction.core.users.UsersRepository;
import eu.attempto.dutch_auction.events.EventsService;
import eu.attempto.dutch_auction.exceptions.BadRequestException;
import eu.attempto.dutch_auction.schedules.ScheduleType;
import eu.attempto.dutch_auction.schedules.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class BidsService {
  private final BidsRepository bidsRepository;
  private final UsersRepository usersRepository;
  private final AuctionsService auctionsService;
  private final SchedulesService schedulesService;
  private final EventsService eventsService;

  private final BigDecimal bidPrice = new BigDecimal(1);

  public String placeBid(Authentication authentication, PlaceBidDto placeBidDto) {
    var auction = auctionsService.getAuctionById(placeBidDto.getAuctionId());
    var user = (User) authentication.getPrincipal();

    validateBid(user, auction);
    schedulesService.removeAuctionSchedule(auction.getId(), ScheduleType.FINISH);

    var bid = Bid.builder().user(user).auction(auction).build();

    CompletableFuture<User> future1 = CompletableFuture.supplyAsync(() -> payForBid(user));
    CompletableFuture<Auction> future2 =
        CompletableFuture.supplyAsync(() -> auctionsService.placeBid(auction));
    CompletableFuture<Bid> future3 = CompletableFuture.supplyAsync(() -> bidsRepository.save(bid));

    // Wait for all tasks to complete
    CompletableFuture.allOf(future1, future2, future3).join();

    schedulesService.scheduleAuctionFinish(auction, user);
    eventsService.addEvent(auction);

    return "OK";
  }

  private void validateBid(User user, Auction auction) {
    if (!auction.getActive()) {
      throw new BadRequestException("This auction is not active");
    }

    if (Objects.equals(user.getId(), auction.getAuthor().getId())) {
      throw new BadRequestException("You can't bid on your own auction");
    }

    BigDecimal comparisonValue = auction.getPrice().multiply(new BigDecimal(2)).add(bidPrice);

    if (user.getBalance().compareTo(comparisonValue) < 0) {
      throw new BadRequestException(
          "You don't have enough coins to make a bid and pay for the lot");
    }
  }

  private User payForBid(User user) {
    user.setBalance(user.getBalance().subtract(bidPrice));
    return usersRepository.save(user);
  }

  public Bid[] getUserBids(User user) {
    return bidsRepository.findByUser(user);
  }

  public Bid[] deleteUserBids(User user) {
    return bidsRepository.deleteByUser(user);
  }
}
