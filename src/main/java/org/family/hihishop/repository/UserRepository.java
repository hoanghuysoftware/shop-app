package org.family.hihishop.repository;

import org.family.hihishop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByPhoneNumber(String phoneNumber);
}
