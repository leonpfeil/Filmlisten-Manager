package com.sep.server.dbaccess;

import com.sep.server.model.RateMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateMovieRepository extends JpaRepository<RateMovie, String> {

    boolean existsByRatingname (String ratingname);

    RateMovie getRateMovieByRatingname(String ratingname);

    RateMovie getRateMovieByMovieName (String moviename);

    RateMovie getRateMovieByUsername (String username);

    List<RateMovie> getAllByUsernameEqualsAndRatingEquals(String username, int rating);

}
