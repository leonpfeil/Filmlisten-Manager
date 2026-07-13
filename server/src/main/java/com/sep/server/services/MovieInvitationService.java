package com.sep.server.services;

import com.sep.server.dbaccess.MovieInvitationRepository;
import com.sep.server.model.MovieInvitation;
import com.sep.server.model.Report;
import com.sep.server.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieInvitationService {
    private MovieInvitationRepository movieInvitationRepository;

    public MovieInvitationService(MovieInvitationRepository movieInvitationRepository) {
        this.movieInvitationRepository = movieInvitationRepository;
    }

    public ResponseEntity<String> createMovieInvitation(MovieInvitation movieInvitation)  {
        movieInvitationRepository.save(movieInvitation);
        String response ="created: "+movieInvitation.toString();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public ResponseEntity<List<MovieInvitation>> getAll(String target){
        List<MovieInvitation> movieInvitations = movieInvitationRepository.findAllByTarget(target);
        return new ResponseEntity(movieInvitations, HttpStatus.OK);
    }
}
