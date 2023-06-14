package eu.attempto.dutch_auction.core.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.attempto.dutch_auction.core.roles.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "_users",
    indexes = @Index(columnList = "email"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"id", "email"}))
public class User implements UserDetails {
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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList(role.getName().name());
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
