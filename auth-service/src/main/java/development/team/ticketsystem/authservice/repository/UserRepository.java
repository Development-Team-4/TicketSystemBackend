package development.team.ticketsystem.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import development.team.ticketsystem.authservice.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}