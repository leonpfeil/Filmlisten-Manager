package com.sep.server.dbaccess;

import com.sep.server.model.MovieInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MovieInvitationRepository extends JpaRepository<MovieInvitation, String> {
    List<MovieInvitation> findMovieInvitationByDoneEquals(Boolean done);
    MovieInvitation getMovieInvitationBySender(String sender);

    List<MovieInvitation> findAllByTarget(String target);
}
