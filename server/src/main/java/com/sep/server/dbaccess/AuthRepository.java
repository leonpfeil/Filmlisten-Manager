package com.sep.server.dbaccess;

import com.sep.server.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Auth, String> {
        String getAuthByUsername(String username);
}
