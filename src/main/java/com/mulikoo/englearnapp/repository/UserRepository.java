package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Optional<User> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    boolean existsByUsername(String username);

}


