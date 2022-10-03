package org.royllo.explorer.backend.repository.user;

import org.royllo.explorer.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link User} repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by its username.
     *
     * @param username username
     * @return user
     */
    Optional<User> findByUsername(String username);

}
