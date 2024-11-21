package com.celfocus.hiring.kickstarter.db.repo;

import com.celfocus.hiring.kickstarter.db.entity.CartItemEntity;
import com.celfocus.hiring.kickstarter.db.entity.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, CartItemPK> {

}
