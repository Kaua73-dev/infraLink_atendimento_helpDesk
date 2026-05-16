package com.infraLink.API.model.repository.user;

import com.infraLink.API.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Integer, User> {

    Optional<User> findByEmail(String email);

}
