package com.sep.server.api;

import com.sep.server.dbaccess.MovieInvitationRepository;
import com.sep.server.model.MovieInvitation;
import com.sep.server.services.MovieInvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
public class MovieInvitationRestController {
    private MovieInvitationRepository movieInvitationRepository;
    private MovieInvitationService movieInvitationService;
    Map<String, String> mappedMovieInvitationList = new LinkedHashMap<String,String>();

    public MovieInvitationRestController(MovieInvitationRepository movieInvitationRepository, MovieInvitationService movieInvitationService) {
        this.movieInvitationRepository = movieInvitationRepository;
        this.movieInvitationService = movieInvitationService;
    }

    @GetMapping(path = "movieInvitation/consumeMovieInvitationRequest")
    public String consumeMovieInvitationRequest(String username) {
        if (mappedMovieInvitationList.containsKey(username)) {
            String name = mappedMovieInvitationList.get(username);
            mappedMovieInvitationList.remove(username);
            return name;
        }

        return "";
    }

    @PostMapping(path = "movieInvitation/addRequest")
    void addRequest(String requested,String requester) {
        mappedMovieInvitationList.put(requested,requester);
    }

    @PostMapping(path = "movieInvitation/change")
    public ResponseEntity<String> changeToDone(@RequestParam(value = "sender") String sender, @RequestParam(value = "target") String target, @RequestParam(value = "movieName") String movieName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("update movie_invitation set done = b'1' where sender like '"+sender+"' and target like '"+target+"' and movie_name like '"+movieName+"'");
            preparedStatement.execute();

            return new ResponseEntity("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping(path = "movieInvitation/add")
    public ResponseEntity<String> addInvitation(@RequestBody MovieInvitation movieInvitation) {
        return movieInvitationService.createMovieInvitation(movieInvitation);
    }

    @PostMapping(path = "movieInvitation/delete")
    public ResponseEntity<String> deleteMovieInvitation(@RequestParam(value = "sender") String sender, @RequestParam(value = "target") String target, @RequestParam(value = "movieName") String movieName) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("delete from movie_invitation where sender like '"+sender+"' and target like '"+target+"' and movie_name like '"+movieName+"'");
            preparedStatement.execute();

            return new ResponseEntity("Success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Error", HttpStatus.OK);
        }
    }

    @GetMapping(path = "movieInvitation/getAllByDone")
    public ResponseEntity<List<MovieInvitation>> getAllDoneInvitations(@RequestParam(value = "search") String search) {
        List<MovieInvitation> allDoneInvitations = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("select * from movie_invitation where target like '"+search+"' and done = b'1'");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String movie_name = resultSet.getString("movie_name");
                String date = resultSet.getString("date");
                Boolean done = resultSet.getBoolean("done");
                String sender = resultSet.getString("sender");
                String target = resultSet.getString("target");
                String text = resultSet.getString("text");
                String time = resultSet.getString("time");

                MovieInvitation movieInvitation = new MovieInvitation();

                movieInvitation.setMovieName(movie_name);
                movieInvitation.setDate(date);
                movieInvitation.setDone(done);
                movieInvitation.setSender(sender);
                movieInvitation.setTarget(target);
                movieInvitation.setText(text);
                movieInvitation.setTime(time);

                allDoneInvitations.add(movieInvitation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<List<MovieInvitation>>(allDoneInvitations, HttpStatus.OK);
    }

    @GetMapping(path = "movieInvitation/getAllByNotDone")
    public ResponseEntity<List<MovieInvitation>> getAll(@RequestParam(value = "search") String search) {
        List<MovieInvitation> allInvitations = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/database", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("select * from movie_invitation where target like '"+search+"' and done = b'0'");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String movie_name = resultSet.getString("movie_name");
                String date = resultSet.getString("date");
                Boolean done = resultSet.getBoolean("done");
                String sender = resultSet.getString("sender");
                String target = resultSet.getString("target");
                String text = resultSet.getString("text");
                String time = resultSet.getString("time");

                MovieInvitation movieInvitation = new MovieInvitation();

                movieInvitation.setMovieName(movie_name);
                movieInvitation.setDate(date);
                movieInvitation.setDone(done);
                movieInvitation.setSender(sender);
                movieInvitation.setTarget(target);
                movieInvitation.setText(text);
                movieInvitation.setTime(time);

                allInvitations.add(movieInvitation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<List<MovieInvitation>>(allInvitations, HttpStatus.OK);
    }
}
