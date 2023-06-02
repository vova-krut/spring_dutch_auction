package eu.attempto.dutch_auction.core.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.attempto.dutch_auction.core.roles.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_users", indexes = @Index(columnList = "email"))
public class User {
  @Id @GeneratedValue private Long id;

  private String email;

  @JsonIgnore private String password;

  private String name;

  @Builder.Default private BigDecimal balance = new BigDecimal(0);

  @ManyToOne
  @JoinColumn(nullable = false)
  private Role role;

  @JsonIgnore
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;
}
