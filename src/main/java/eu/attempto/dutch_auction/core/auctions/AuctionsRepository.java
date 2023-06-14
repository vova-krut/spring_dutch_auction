package eu.attempto.dutch_auction.core.auctions;

import eu.attempto.dutch_auction.core.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface AuctionsRepository extends JpaRepository<Auction, Long> {
  List<Auction> deleteByAuthor(User author);

  List<Auction> findByActiveTrueAndEndTimeAfter(ZonedDateTime dateTime);

  List<Auction> findByActiveFalseAndStartTimeBefore(ZonedDateTime dateTime);

  List<Auction> findByActiveTrueAndEndTimeLessThanEqual(ZonedDateTime dateTime);
}
