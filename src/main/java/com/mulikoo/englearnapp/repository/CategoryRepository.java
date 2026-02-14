package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.Word;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    @Query("select id from category where uid = :uid")
    Optional<Long> findIdByUid(@Param("uid") UUID uid);

    @Query("select uid from category where id = :id")
    Optional<UUID> findUidById(@Param("id") Long id);

    @Query("select id from category where name = :name")
    Optional<Long> findIdByName(@Param("name") String name);
}
