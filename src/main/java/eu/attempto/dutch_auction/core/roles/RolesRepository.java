package eu.attempto.dutch_auction.core.roles;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(RolesEnum name);
}
