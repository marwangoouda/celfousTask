package com.celfocus.hiring.kickstarter.db.repo;

import com.celfocus.hiring.kickstarter.db.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserId(String username);

    @Modifying
    void deleteByUserId(String username);
}
