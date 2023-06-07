package eu.attempto.dutch_auction.core.auctions;

import eu.attempto.dutch_auction.core.auctions.dto.ChangeAuctionDto;
import eu.attempto.dutch_auction.core.auctions.dto.CreateAuctionDto;
import eu.attempto.dutch_auction.core.auctions.images.Image;
import eu.attempto.dutch_auction.core.auctions.images.ImagesService;
import eu.attempto.dutch_auction.core.users.User;
import eu.attempto.dutch_auction.events.EventsService;
import eu.attempto.dutch_auction.exceptions.BadRequestException;
import eu.attempto.dutch_auction.schedules.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class AuctionsService {
  private final AuctionsRepository auctionsRepository;
  private final ImagesService imagesService;
  private final SchedulesService schedulesService;
  private final EventsService eventsService;

  public Auction createAuction(
      Authentication authentication,
      MultipartFile[] imageFiles,
      CreateAuctionDto createAuctionDto) {
    var auction =
        Auction.builder()
            .author((User) authentication.getPrincipal())
            .title(createAuctionDto.getTitle())
            .description(createAuctionDto.getDescription())
            .endTime(createAuctionDto.getEndTime())
            .price(createAuctionDto.getPrice())
            .build();

    if (createAuctionDto.getStartTime() != null) {
      auction.setActive(false);
      auction.setStartTime(createAuctionDto.getStartTime());
    }

    auctionsRepository.save(auction);

    var images = new ArrayList<Image>();
    for (var imageFile : imageFiles) {
      var image = imagesService.saveImage(auction, imageFile);
      images.add(image);
    }
    auction.setImages(images);

    schedulesService.scheduleAuctionFinish(auction, null);
    if (auction.getStartTime() != null) {
      schedulesService.scheduleAuctionStart(auction);
    }

    return auction;
  }

  public Page<Auction> getAuctions(Pageable pageable) {
    return auctionsRepository.findAll(pageable);
  }

  public Auction[] deleteUserAuctions(User user) {
    return auctionsRepository.deleteByAuthor(user);
  }

  public Auction getAuctionById(Long id) {
    var auction = auctionsRepository.findById(id);
    if (auction.isEmpty()) {
      throw new BadRequestException("Auction with this id was not found");
    }

    return auction.get();
  }

  public Flux<Auction> subscribeToAuction(Long id) {
    return eventsService.addEventAndSubscribe(getAuctionById(id));
  }

  public Auction placeBid(Auction auction) {
    var bidCost = new BigDecimal("0.1");
    auction.setPrice(auction.getPrice().subtract(bidCost));
    auction.setEndTime(calculateNewEndTime(auction.getEndTime()));

    return auctionsRepository.save(auction);
  }

  private ZonedDateTime calculateNewEndTime(ZonedDateTime endTime) {
    var fiveMinutes = 300_000;
    var twoAndHalfMinutes = 150_000;
    var oneMin = 60_000;

    long timeDiff = ChronoUnit.MILLIS.between(ZonedDateTime.now(), endTime);
    if (timeDiff <= fiveMinutes && timeDiff > twoAndHalfMinutes) {
      return ZonedDateTime.now().plusMinutes(5);
    }
    if (timeDiff <= twoAndHalfMinutes && timeDiff > oneMin) {
      return ZonedDateTime.now().plusMinutes(2).plusSeconds(30);
    }
    if (timeDiff <= oneMin) {
      return ZonedDateTime.now().plusMinutes(1);
    }

    return endTime;
  }

  public void changeAuction(ChangeAuctionDto changeAuctionDto) {
    var auction = getAuctionById(changeAuctionDto.getId());
    auction.setPrice(changeAuctionDto.getPrice());

    eventsService.addEvent(auction);
    auctionsRepository.save(auction);
  }
}
