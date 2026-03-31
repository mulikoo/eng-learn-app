package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(value = "User.userAndCurrentCategory")
    Optional<User> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    boolean existsByUsername(String username);

    @EntityGraph(value = "User.userAndCurrentCategory")
    Optional<User> findByUsername(String username);

    @Override
    @EntityGraph(value = "User.userAndCurrentCategory")
    Page<User> findAll(Pageable pageable);
}


