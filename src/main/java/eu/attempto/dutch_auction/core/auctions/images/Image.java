package eu.attempto.dutch_auction.core.auctions.images;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.attempto.dutch_auction.core.auctions.Auction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = @Index(columnList = "auctionId"))
public class Image {
  @Id @GeneratedValue private Long id;

  @ManyToOne
  @JoinColumn(nullable = false, name = "auctionId")
  @JsonIgnore
  private Auction auction;

  private String link;

  @JsonIgnore
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;
}
