package com.sep.server.dbaccess;

import com.sep.server.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,String>{
    Boolean existsByMovieName(String name);
    Movie getMovieByMovieName(String movieName);

}
