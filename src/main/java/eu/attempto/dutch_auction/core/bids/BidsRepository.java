package eu.attempto.dutch_auction.core.bids;

import eu.attempto.dutch_auction.core.auctions.Auction;
import eu.attempto.dutch_auction.core.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidsRepository extends JpaRepository<Bid, Long> {
  List<Bid> findByUser(User user);

  List<Bid> deleteByUser(User user);

  List<Bid> findByAuctionOrderByCreatedAtDesc(Auction auction);
}
