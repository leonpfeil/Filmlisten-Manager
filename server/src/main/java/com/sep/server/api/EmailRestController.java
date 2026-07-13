package com.sep.server.api;

import com.sep.server.dbaccess.AuthRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.Auth;
import com.sep.server.model.User;
import com.sep.server.services.EmailService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import java.util.Random;

//https://www.tutorialspoint.com/spring_boot/spring_boot_sending_email.htm
@RestController
public class EmailRestController {
    @Autowired
    private EmailService emailService;
    private UserRepository userRepository;
    private AuthRepository authRepository;

    public EmailRestController(EmailService emailService, UserRepository userRepository, AuthRepository authRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.authRepository = authRepository;
    }

    @GetMapping (path = "email/send")
    public ResponseEntity<String> sendEmailWithCode(@RequestParam(value = "receiver") String receiver) throws MessagingException {
        String code = generateRandomNumber();

        emailService.sendEmail(receiver, code);

        User user = userRepository.getUserByEmail(receiver);
        String username = user.getUsername();

        Auth auth = new Auth();
        auth.setUsername(username);
        auth.setCode(code);
        authRepository.save(auth);

        return new ResponseEntity("Success", HttpStatus.OK);
    }

    private String generateRandomNumber() {
        Random random = new Random();

        return String.format("%04d%n", random.nextInt(10000));
    }

    @GetMapping(path = "email/MovieInvitationAccepted")
    public ResponseEntity<String> sendAcceptEmail(@RequestParam(value = "target") String target) {
        emailService.sendAcceptEmail(target);

        return new ResponseEntity("Success", HttpStatus.OK);
    }

    @GetMapping(path = "email/MovieInvitationDeclined")
    public ResponseEntity<String> sendDeclinedEmail(@RequestParam(value = "target") String target) {
        emailService.sendDeclinedEmail(target);

        return new ResponseEntity("Success", HttpStatus.OK);
    }

    @GetMapping(path = "email/MovieInvitationGet")
    public ResponseEntity<String> sendMovieInvitationEmail(@RequestParam(value = "target") String target, @RequestParam(value = "sender") String sender, @RequestParam(value = "movieName") String movieName, @RequestParam(value = "date") String date, @RequestParam(value = "time") String time) {
        emailService.sendMovieInvitationEmail(target, sender, movieName, date, time);

        return new ResponseEntity("Success", HttpStatus.OK);
    }
}
