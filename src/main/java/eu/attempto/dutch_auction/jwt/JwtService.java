package eu.attempto.dutch_auction.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import eu.attempto.dutch_auction.core.users.UsersService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtService {
  @Value("${security.secret-key}")
  private String secretKey;

  private final UsersService usersService;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String createToken(Long id) {
    var algorithm = Algorithm.HMAC256(secretKey);
    return JWT.create()
        .withSubject(id.toString())
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
        .sign(algorithm);
  }

  public Authentication validateToken(String token) {
    try {
      var algorithm = Algorithm.HMAC256(secretKey);

      var verifier = JWT.require(algorithm).build();
      var decoded = verifier.verify(token);

      var userContainer = usersService.findUserById(Long.parseLong(decoded.getSubject()));
      if (userContainer.isEmpty()) {
        return null;
      }

      var user = userContainer.get();

      return new UsernamePasswordAuthenticationToken(
          user, null, AuthorityUtils.createAuthorityList(user.getRole().getName().name()));
    } catch (JWTVerificationException e) {
      return null;
    }
  }
}
