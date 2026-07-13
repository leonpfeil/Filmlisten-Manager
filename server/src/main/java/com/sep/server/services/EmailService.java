package com.sep.server.services;

import com.sep.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private String email = "SEPGruppeS@gmail.com";

    public void sendEmail(String receiver, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(email);
        message.setTo(receiver);
        message.setText("Your KinoS authentification code is: "+code);
        message.setSubject("KinoS Authentification Code");

        mailSender.send(message);
        System.out.println("Email sent successfully!");
    }

    public void sendMovieInvitationEmail(String receiver, String sender, String movieName, String date, String time) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(email);
        message.setTo(receiver);
        message.setText(sender+" sent you a movie invitation to the movie "+movieName+" at "+time+", "+date+"!\n\nCheck your KinoS account for more information!");
        message.setSubject("KinoS Movie Invitation from "+sender);

        mailSender.send(message);
        System.out.println("Email sent successfully!");
    }

    public void sendAcceptEmail(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(email);
        message.setTo(receiver);
        message.setText("Your KinoS movie invitation has been accepted!");
        message.setSubject("KinoS Movie Invitation accepted");

        mailSender.send(message);
        System.out.println("Email sent successfully!");
    }

    public void sendDeclinedEmail(String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(email);
        message.setTo(receiver);
        message.setText("Your movie invitation has been declined!");
        message.setSubject("KinoS Movie Invitation");

        mailSender.send(message);
        System.out.println("Email sent successfully!");
    }

    public void sendReportEmail(List<User> admins, long reportID, String movieName, String reportMessage) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(email);

        message.setText("New Report to movie \""+ movieName + "\" with report message: "+reportMessage);
        message.setSubject("New Report: "+reportID);

        for(User u : admins) {
            message.setTo(u.getEmail());
            mailSender.send(message);
            System.out.println("Email sent successfully!");
        }
    }
}
