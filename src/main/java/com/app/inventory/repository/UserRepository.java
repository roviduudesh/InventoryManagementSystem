package com.app.inventory.repository;


import com.app.inventory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    List<User> findAllByOrderByFirstNameAsc();
}
