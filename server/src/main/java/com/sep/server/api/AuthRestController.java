package com.sep.server.api;

import com.sep.server.dbaccess.AuthRepository;
import com.sep.server.model.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@RestController
public class AuthRestController {
    private AuthRepository authRepository;

    public AuthRestController(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @GetMapping(path = "auth/code")
    public ResponseEntity<String> getCodeFromAuth(@RequestParam(value = "username") String username) {
            Auth auth = authRepository.getById(username);
            String code = auth.getCode();
            return new ResponseEntity(code, HttpStatus.OK);
    }

    @GetMapping(path = "auth/drop")
    public ResponseEntity<String> deleteFromAuth() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("delete from auth");
            preparedStatement.execute();

            return new ResponseEntity("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.OK);
        }
    }
}
