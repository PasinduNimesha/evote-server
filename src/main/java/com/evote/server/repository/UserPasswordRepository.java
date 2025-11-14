package com.evote.server.repository;

import com.evote.server.model.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordRepository extends JpaRepository<UserPassword, String> {
}