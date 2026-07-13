package com.sep.server.dbaccess;

import com.sep.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    //gegeben durch Springframework, gibt ein Bool-wert, ob der String/ Eintrag in der Datenbank existiert
    Boolean existsByUsername(String username);
    Boolean existsByPassword(String password);
    Boolean existsByEmail(String email);
    Boolean existsByPasswordAndUsername(String password, String username);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    List<User> findUserByUsernameContaining(String username);
    List<User> findUserByIsAdmin(boolean bool);

}
