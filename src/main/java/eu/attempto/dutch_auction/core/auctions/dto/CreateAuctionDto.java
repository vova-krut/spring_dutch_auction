package eu.attempto.dutch_auction.core.auctions.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuctionDto {
  private String title;

  private String description;

  @Nullable private ZonedDateTime startTime;

  private ZonedDateTime endTime;

  private BigDecimal price;
}
