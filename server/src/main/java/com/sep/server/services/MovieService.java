package com.sep.server.services;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sep.server.dbaccess.MovieRepository;

import com.sep.server.model.Movie;
import com.sep.server.model.RateMovie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.io.IOException;
import java.sql.*;
import java.util.List;


@Service
public class MovieService {

    private static MovieRepository movieRepository;

    private String filmlengthFilter = "";
    private String releaseYearFilter = "";
    private String regisseurFilter = "";
    private String directorFilter = "";
    private String castFilter = "";
    private String categoryFilter = "";

    public MovieService(MovieRepository movieRepository){this.movieRepository=movieRepository;}

    public static ResponseEntity<String> createMovie(Movie movie){                 //Filme hinzufügen
        if(!movieRepository.existsByMovieName(movie.getMovieName())) {//Testen ob der Film in der Datenbank ist
            try {
                movieRepository.save(movie);                                    // Film speichern
                return new ResponseEntity("created: " + movie.getMovieName(), HttpStatus.OK);
            }
            catch (Exception e){
                return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{return new ResponseEntity("Already in Database",HttpStatus.IM_USED);}
    }

    public ResponseEntity<String> deleteMovie(Movie movie) {
        if(movieRepository.existsByMovieName(movie.getMovieName())) {
            movieRepository.deleteById(movie.getMovieName());
            return new ResponseEntity("deleted " + movie.getMovieName(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(movie.getMovieName()+" not found", HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<Movie> getMovieWithID(String searchVal){
        Movie movie=getMovie(searchVal);

        if(movie!=null) {
            return new ResponseEntity(movie, HttpStatus.OK);
        }
        else{return new ResponseEntity(HttpStatus.NOT_FOUND);}
    }

    public ResponseEntity<Boolean> isMovieInWatchlist(String userName, String movieName) {
        Boolean inWatchlist = true;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getWatchlist = con.prepareStatement("select watchlist from user_profile where user_username='"+userName+"' and watchlist like '%"+movieName+"%'");
            ResultSet watchlist = getWatchlist.executeQuery();
            watchlist.next();
            if(watchlist.getRow()==0) {
                inWatchlist = false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(inWatchlist, HttpStatus.OK);
    }

    public ResponseEntity<Boolean> isMovieInWatchedlist(String userName, String movieName) {
        Boolean inWatchedlist = true;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getWatchedlist = con.prepareStatement("select watchedlist from user_profile where user_username='"+userName+"' and watchedlist like '%"+movieName+"%'");
            ResultSet watchedlist = getWatchedlist.executeQuery();
            watchedlist.next();
            if(watchedlist.getRow()==0) {
                inWatchedlist = false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(inWatchedlist, HttpStatus.OK);
    }

    public ResponseEntity<String> addToWatchlist(String moviename, String username) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getWatchlist = con.prepareStatement("Select watchlist From user_profile Where user_username='"+username+"'");
            ResultSet watchlist = getWatchlist.executeQuery();
            watchlist.next();
            String currentWatchlist = "";
            if(watchlist.getString("watchlist") != null && !watchlist.getString("watchlist").equals("")) {

                currentWatchlist = watchlist.getString("watchlist");

                currentWatchlist = currentWatchlist.replaceAll("'","''");

                PreparedStatement updateWatchlist = con.prepareStatement("Update user_profile Set watchlist='"+currentWatchlist+",#,"+moviename+"'"+" Where user_username='"+username+"'");
                updateWatchlist.execute();
            }
            else {
                PreparedStatement updateWatchlist = con.prepareStatement("Update user_profile Set watchlist='" + currentWatchlist + moviename + "'" + " Where user_username='" + username + "'");
                updateWatchlist.execute();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity("funktioniert", HttpStatus.OK);
    }

    public ResponseEntity<Boolean> areFiltersEnabled(String username) {
        boolean filtersEnabled = false;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement filters = con.prepareStatement("Select * From filter Where user_profile_user_username='"+username+"'");
            ResultSet filter = filters.executeQuery();
            filter.next();
            if(!filter.getString("cast_filter").equals("") && filter.getString("cast_filter") != null) {
                filtersEnabled = true;
            }
            else if(!filter.getString("category_filter").equals("") && filter.getString("category_filter") != null) {
                filtersEnabled = true;
            }
            else if(!filter.getString("director_filter").equals("") && filter.getString("director_filter") != null) {
                filtersEnabled = true;
            }
            else if(!filter.getString("filmlength_filter").equals("") && filter.getString("filmlength_filter") != null) {
                filtersEnabled = true;
            }
            else if(!filter.getString("regisseur_filter").equals("") && filter.getString("regisseur_filter") != null) {
                filtersEnabled = true;
            }
            else if(!filter.getString("release_year_filter").equals("") && filter.getString("release_year_filter") != null) {
                filtersEnabled = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*/if(filmlengthFilter.equals("") && releaseYearFilter.equals("") && regisseurFilter.equals("") && directorFilter.equals("") && castFilter.equals("")
                && categoryFilter.equals("")) {
            filtersEnabled = false;
        }/*/
        return new ResponseEntity(filtersEnabled, HttpStatus.OK);
    }

    public ResponseEntity<List<String>> getFilters(String username) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getFilter = con.prepareStatement("SELECT `cast_filter`,`category_filter`,`director_filter`,`filmlength_filter`,`regisseur_filter`,`release_year_filter` FROM filter WHERE `user_profile_user_username`='" + username +"'");
            ResultSet filter = getFilter.executeQuery();
            List<String> filters = new ArrayList<>();
            while(filter.next()) {
                filters.add(filter.getString("filmlength_filter"));
                filters.add(filter.getString("release_year_filter"));
                filters.add(filter.getString("regisseur_filter"));
                filters.add(filter.getString("director_filter"));
                filters.add(filter.getString("cast_filter"));
                filters.add(filter.getString("category_filter"));
            }
            return new ResponseEntity(filters, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> setFilter(String username, String filmlength, String releaseYear, String regisseur, String director, String cast, String category) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getFilter = con.prepareStatement("SELECT `cast_filter`,`category_filter`,`director_filter`,`filmlength_filter`,`regisseur_filter`,`release_year_filter` FROM filter WHERE `user_profile_user_username`='" + username + "'");
            ResultSet checkIfEmpty = getFilter.executeQuery();
            checkIfEmpty.next();
            if(checkIfEmpty.getRow() == 0) {
                PreparedStatement setFilter = con.prepareStatement("INSERT INTO filter(`cast_filter`, `category_filter`, `director_filter`, `filmlength_filter`, `regisseur_filter`, `release_year_filter`, `user_profile_user_username`) VALUES ('" +
                        cast+"','"+category+"','"+director+"','"+filmlength+"','"+regisseur+"','"+releaseYear+"','"+username+"')");
                setFilter.execute();
            }
            if(checkIfEmpty.getRow() == 1) {
                PreparedStatement setFilter = con.prepareStatement("UPDATE filter SET `cast_filter`='"+cast+"',"+"`category_filter`='"+category+"',"+"`director_filter`='"+director+"',"+"`filmlength_filter`='"+filmlength+"',"+
                        "`regisseur_filter`='"+regisseur+"',"+"`release_year_filter`='"+releaseYear+"' WHERE `user_profile_user_username`='"+username+"'");
                setFilter.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

        public ResponseEntity<String> addToWatchedlist(String moviename, String username) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getWatchedlist = con.prepareStatement("Select watchedlist From user_profile Where user_username='"+username+"'");
            ResultSet watchedlist = getWatchedlist.executeQuery();
            watchedlist.next();
            String currentWatchedlist = "";
            if(watchedlist.getString("watchedlist") != null && !watchedlist.getString("watchedlist").equals("")) {
                currentWatchedlist = watchedlist.getString("watchedlist");
                currentWatchedlist = currentWatchedlist.replaceAll("'","''");
                PreparedStatement updateWatchlist = con.prepareStatement("Update user_profile Set watchedlist='"+currentWatchedlist+",#,"+moviename+"'"+" Where user_username='"+username+"'");
                updateWatchlist.execute();
            }
            else {
                PreparedStatement updateWatchedlist = con.prepareStatement("Update user_profile Set watchedlist='" + currentWatchedlist + moviename + "'" + " Where user_username='" + username + "'");
                updateWatchedlist.execute();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<String> deleteFromWatchlist(String moviename, String username) {

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getWatchlist = con.prepareStatement("Select watchlist From user_profile Where user_username='" + username + "'");
            ResultSet watchlist = getWatchlist.executeQuery();
            watchlist.next();

            if (watchlist.getString("watchlist") != null && !watchlist.getString("watchlist").equals("")) {
                if(watchlist.getString("watchlist").contains(",#,")) {
                    if(watchlist.getString("watchlist").contains(moviename + ",#,")) {
                        String newWatchlist = watchlist.getString("watchlist").replaceAll(moviename + ",#,", "");
                        PreparedStatement updateWatchlist = con.prepareStatement("Update user_profile Set watchlist='" + newWatchlist + "'" + " Where user_username='" + username + "'");
                        updateWatchlist.execute();
                    }
                    else if(watchlist.getString("watchlist").contains(",#,"+moviename)) {
                        String newWatchlist = watchlist.getString("watchlist").replaceAll(",#," + moviename, "");
                        PreparedStatement updateWatchlist = con.prepareStatement("Update user_profile Set watchlist='" + newWatchlist + "'" + " Where user_username='" + username + "'");
                        updateWatchlist.execute();
                    }
                }
                else {
                    String newWatchlist = watchlist.getString("watchlist").replaceAll(""+moviename, "");
                    PreparedStatement updateWatchlist = con.prepareStatement("Update user_profile Set watchlist='" + newWatchlist + "'" + " Where user_username='" + username + "'");
                    updateWatchlist.execute();
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<String> deleteFromWatchedlist(String moviename, String username) {

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement getWatchedlist = con.prepareStatement("Select watchedlist From user_profile Where user_username='" + username + "'");
            ResultSet watchedlist = getWatchedlist.executeQuery();
            watchedlist.next();

            if (watchedlist.getString("watchedlist") != null && !watchedlist.getString("watchedlist").equals("")) {
                if(watchedlist.getString("watchedlist").contains(",#,")) {
                    if(watchedlist.getString("watchedlist").contains(moviename + ",#,")) {
                        String newWatchedlist = watchedlist.getString("watchedlist").replaceAll(moviename + ",#,", "");
                        PreparedStatement updateWatchedlist = con.prepareStatement("Update user_profile Set watchedlist='" + newWatchedlist + "'" + " Where user_username='" + username + "'");
                        updateWatchedlist.execute();
                    }
                    else if(watchedlist.getString("watchedlist").contains(",#,"+moviename)) {
                        String newWatchedlist = watchedlist.getString("watchedlist").replaceAll(",#," + moviename, "");
                        PreparedStatement updateWatchedlist = con.prepareStatement("Update user_profile Set watchedlist='" + newWatchedlist + "'" + " Where user_username='" + username + "'");
                        updateWatchedlist.execute();
                    }
                }
                else {
                    String newWatchedlist = watchedlist.getString("watchedlist").replaceAll(""+moviename, "");
                    PreparedStatement updateWatchedlist = con.prepareStatement("Update user_profile Set watchedlist='" + newWatchedlist + "'" + " Where user_username='" + username + "'");
                    updateWatchedlist.execute();
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new ResponseEntity(HttpStatus.OK);
    }


    public ResponseEntity<List<Movie>> getSpecificMovie(String searchVal) {
        List<String> movie = new ArrayList<>();
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from movie where movie_name like '"+searchVal+"'");
            ResultSet rs = state.executeQuery();
            while(rs.next()) {
                movie.add(rs.getString("movie_name"));
                movie.add(rs.getString("author"));
                movie.add(rs.getString("cast"));
                movie.add(rs.getString("category"));
                movie.add(rs.getString("director"));
                movie.add(rs.getString("length"));
                movie.add(rs.getString("release_year"));
                movie.add(rs.getString("banner_path"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(movie, HttpStatus.OK);
    }

    public ResponseEntity<String> createBanner(Movie movie){

        String movieName = movie.getMovieName().replaceAll("[\\\\\\/:\\*?\"<>\\|]","");
        try
        {
            Path realPath = Paths.get("banner");
            Files.createDirectories(realPath);
            File file = new File(realPath.toString() + "/" + movieName + ".jpg");
            File bannerFile = new File(movie.getBannerPath());
            BufferedImage banner = ImageIO.read(bannerFile);
            ImageIO.write(banner,"jpg",file);
            movie.setBannerPath(file.getAbsolutePath());
            movieRepository.save(movie);
        }
        catch (Exception e)
        {
            System.out.println("Error: Couldnt write image");
        }
        return new ResponseEntity("Banner uploaded.", HttpStatus.OK);
    }

    public ResponseEntity<List<Movie>> getSpecificMovieNames(String searchVal){
        List<String> movies = new ArrayList<>();
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from movie where movie_name like '%"+searchVal+"%'");
            ResultSet rs = state.executeQuery();

            while (rs.next()) {
                String moviename = rs.getString("movie_name");
                movies.add(moviename);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(movies, HttpStatus.OK);
    }

    public ResponseEntity<List<Movie>> getSpecificMovieNamesWithFilters(String moviename, String filmlength, String releaseYear, String regisseur,
                                                                        String director, String cast, String category){
        List<String> movies = new ArrayList<>();
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from movie where movie_name like '%"+moviename+"%'"+
                    " and length like '%"+filmlength+"%'"+" and release_year like '%"+releaseYear+"%'"+" and author like '%"+regisseur+"%'"+
                    " and director like '%"+director+"%'"+" and cast like '%"+cast+"%'"+" and category like '%"+category+"%'");
            ResultSet rs = state.executeQuery();

            while (rs.next()) {
                String movienames = rs.getString("movie_name");
                movies.add(movienames);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(movies, HttpStatus.OK);
    }

    public ResponseEntity<String> existsMovie(String searchVal) {
        String response;
        if(movieRepository.existsByMovieName(searchVal)){return new ResponseEntity(HttpStatus.FOUND);}
        else{return new ResponseEntity(HttpStatus.NOT_FOUND);}
    }

    public ResponseEntity<File> getBannerOf(String searchVal)  {
        Path path = Paths.get("./banner/"+searchVal+".jpg");
        File banner = new File(path.toString());
        return new ResponseEntity(banner,HttpStatus.OK);
    }


    public ResponseEntity<String> showMovieCover(String searchVal) {
        Movie bannerMovie= movieRepository.getMovieByMovieName(searchVal.replace("''","'"));

        File banner;
        Path path;
        String baseImage="";
        //Bild wird gesucht
        if(bannerMovie!=null&&bannerMovie.getBannerPath()!=null) {
            path = Paths.get(bannerMovie.getBannerPath());
            //Bild wird in Base64-String codeirt
            banner = new File(path.toString());
            byte[] bytes = new byte[(int) banner.length()];
            try {
                FileInputStream fis = new FileInputStream(banner);
                fis.read(bytes);
                fis.close();
                baseImage = Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Fehler beim Banner anzeigen" + e.getMessage());
            }
        }
        return new ResponseEntity(baseImage,HttpStatus.OK);
    }

    public static Movie getMovie(String movieName){
        if(movieRepository.existsByMovieName(movieName)) {
            Movie movie = movieRepository.getMovieByMovieName(movieName);
            return movie;
        }
        else {return null;}
    }

    // Zur Erstellung der Filmvorschläge
    public static List<String> getBestRatedMoviesOfCategory(String category, List<String> watched) {

        List<String> movies = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {

            PreparedStatement state = con.prepareStatement("select * from movie where category like '%" + category + "%' order by globalrating desc");
            ResultSet rs = state.executeQuery();

            int counter = 0;

            while (rs.next()&&counter != 15) {
                boolean add = true;

                for (String m : watched) {
                    if (m.equals(rs.getString("movie_name"))) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    movies.add(rs.getString("movie_name"));
                    counter++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //movieRepository.findAllByCategoryContainsOrderByGlobalratingDesc(category); </3

        return movies;
    }

    public ResponseEntity<String> setGlobalRating(Movie movie) {


        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {

            PreparedStatement state = con.prepareStatement("Update `movie` Set globalrating ='" + movie.getGlobalrating() +"'"+ "Where movie_name='" + movie.getMovieName() +"'");
            state.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity("aktualisiert", HttpStatus.OK);
    }

    public ResponseEntity<String> resetGlobalRating(Movie movie) {


        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {

            PreparedStatement state = con.prepareStatement("Update `movie` Set globalrating ='" + 0 +"'"+ "Where movie_name='" + movie.getMovieName() +"'");
            state.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity("zurückgesetzt", HttpStatus.OK);
    }

    public ResponseEntity<List<Movie>> getGlobal(String name) {

        List<String> movie = new ArrayList<>();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from `movie` where movie_name like '" + name + "'");
            ResultSet a = state.executeQuery();
            while (a.next()) {
                movie.add(a.getString("globalrating"));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(movie, HttpStatus.OK);
    }

    public ResponseEntity<String> getBestMovieRecommendation(String username) {
        List<String> movies = new ArrayList<>();
        if(movieRepository.count()>15) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
                PreparedStatement state = con.prepareStatement("select * from movie order by globalrating desc");
                ResultSet rs = state.executeQuery();
                int counter = 0;
                while (rs.next() && counter < 15) {
                    String moviename = rs.getString("movie_name");
                    movies.add(moviename);
                    counter++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity(movies, HttpStatus.OK);
    }

}