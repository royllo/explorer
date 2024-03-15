package org.royllo.explorer.core.repository.user;

import org.royllo.explorer.core.domain.user.UserLnurlAuthKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link UserLnurlAuthKey} repository.
 */
@Repository
public interface UserLnurlAuthKeyRepository extends JpaRepository<UserLnurlAuthKey, Long> {

    /**
     * Find a user lnurl-auth by a linking key.
     *
     * @param linkingKey linking key
     * @return user lnurl-auth key
     */
    Optional<UserLnurlAuthKey> findByLinkingKey(String linkingKey);

    /**
     * Find a user lnurl-auth by a k1.
     *
     * @param k1 k1
     * @return user lnurl-auth key
     */
    Optional<UserLnurlAuthKey> findByK1(String k1);

}
