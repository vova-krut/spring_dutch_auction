package eu.attempto.dutch_auction.core.bids;

import eu.attempto.dutch_auction.core.auctions.Auction;
import eu.attempto.dutch_auction.core.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidsRepository extends JpaRepository<Bid, Long> {
  Bid[] findByUser(User user);

  Bid[] deleteByUser(User user);

  Optional<Bid> findByAuctionOrderByCreatedAtDesc(Auction auction);
}
