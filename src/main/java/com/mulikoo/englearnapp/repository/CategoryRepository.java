package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {
    Optional<Category> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    @Query("select id from Category where uid = :uid")
    Optional<Long> findIdByUid(@Param("uid") UUID uid);

    @Query("select uid from Category where id = :id")
    Optional<UUID> findUidById(@Param("id") Long id);

    @Query("select id from Category where name = :name")
    Optional<Long> findIdByName(@Param("name") String name);

    boolean existsByName(String name);
}
