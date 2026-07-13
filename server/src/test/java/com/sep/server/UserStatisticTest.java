/*package com.sep.server;


import com.sep.server.dbaccess.MovieRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.Movie;
import com.sep.server.model.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserStatisticTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    private void initialize() {
        createUser();
        User user = userRepository.getUserByUsername("TestUser");

        createMovies("Drama, Comedy", "Cooper Raiff,Dakota Johnson, Evan Assante", 100);
        createMovies("Adventure, Action, Comedy", "Benedict Cumberbatch, Elizabeth Olsen", 107);
        createMovies("Action, Comedy", "Benedict Cumberbatch, Cillian Murphy", 103);

        Movie movieA= movieRepository.getMovieByMovieName("Drama, Comedy"+"Movie");
        Movie movieB= movieRepository.getMovieByMovieName("Adventure, Action, Comedy"+"Movie");
        Movie movieC= movieRepository.getMovieByMovieName("Action, Comedy"+"Movie");

        httpReqeust(movieA,user);
        httpReqeust(movieB,user);
        httpReqeust(movieC, user);

    }

    @Test
    public void statTest(){
        initialize();
        User user = userRepository.getUserByUsername("TestUser");
        String startDateString = LocalDate.now().minusDays(1).toString();
        String endDateString = LocalDate.now().plusDays(1).toString();
        HttpResponse<String> genreString = getStats("/usersProfile/getGenre", user.getUsername(), startDateString, endDateString);
        HttpResponse<String> castString = getStats("/usersProfile/getCast", user.getUsername(), startDateString, endDateString);
        HashMap<String,Integer> cast = createHashMap(castString);
        cast=sortByValueDesc(cast);


        HashMap<String, Integer> genre= createHashMap(genreString);



        HashMap<String,Integer> genreControl= new HashMap<>();
        genreControl.put("Action",2);
        genreControl.put("Adventure",1);
        genreControl.put("Drama",1);
        genreControl.put("Comedy",3);

        HttpResponse<String> filmlengthResponse = getStats("/usersProfile/getFilmlengthStat", user.getUsername(), startDateString, endDateString);
        String filmlengthString = filmlengthResponse.body();

        assertEquals("Anzahl Schauspieler","{Benedict Cumberbatch=2, Dakota Johnson=1,  Cillian Murphy=1,  Elizabeth Olsen=1, Cooper Raiff=1,  Evan Assante=1}",cast.toString());
        assertEquals("Anzahl Genres",genreControl,genre);
        assertEquals("Filmdauer","310",filmlengthString);
        delete();

    }

    private User createUser(){
        User user=new User();
        user.setEmail("TestUser@outlook.de");
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setUsername("TestUser");
        user.setPassword("password");
        user.setDateOfBirth(null);
        user.setAdmin(true);
        user.setPfpImagePath(null);
        user.setTwoFA(false);
        userRepository.save(user);
        return user;
    }

    private void createMovies(String genre, String cast,int time){
        Movie movie=new Movie();
        movie.setAuthor("Test");
        movie.setMovieName(genre+"Movie");
        movie.setBannerPath(null);
        movie.setCast(cast);
        movie.setCategory(genre);
        movie.setLength(time);
        movie.setDirector("Test");
        movie.setReleaseYear(2022);
        movie.setGlobalrating(0);
        movieRepository.save(movie);
    }

    private void delete() {
        movieRepository.delete(movieRepository.getMovieByMovieName("Drama, Comedy" + "Movie"));
        movieRepository.delete(movieRepository.getMovieByMovieName("Adventure, Action, Comedy" + "Movie"));
        movieRepository.delete(movieRepository.getMovieByMovieName("Action, Comedy" + "Movie"));
        userRepository.deleteById("TestUser");

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "");
            PreparedStatement preparedStatement = con.prepareStatement("delete From user_statistic Where username='" + "Testuser" + "'");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void httpReqeust(Movie movie,User user){
        try {
            postUsernameAndMoviename("/movie/addToWatchedlist", replaceIlleagalCharacters(movie.getMovieName()), user.getUsername());
            String curDate = LocalDate.now().toString();
            String filmlength = String.valueOf(movie.getLength());
            postStat("/usersProfile/addStat", user.getUsername(), replaceIlleagalCharacters(curDate), replaceIlleagalCharacters(movie.getCategory()), replaceIlleagalCharacters(movie.getMovieName()), replaceIlleagalCharacters(movie.getCast()), filmlength);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private  HttpResponse<String> postUsernameAndMoviename(String url, String username, String moviename) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(moviename))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private  HttpResponse<String> postStat(String url, String username, String date, String category, String moviename, String cast, String filmlength) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username+"&moviename="+moviename+"&date="+date+"&cast="+cast+"&category="+category+"&filmlength="+filmlength))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(moviename))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private  HttpResponse<String> getStats(String url, String username, String startDate, String endDate) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username+"&startDate="+startDate+"&endDate="+endDate))
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String replaceIlleagalCharacters(String string){
        return string.replaceAll(" ", "%20")
                .replaceAll("!","%21")
                .replaceAll("\"","%22")
                .replaceAll("#","%23")
                .replaceAll("\\$","%24")
                .replaceAll("&","%26")
                .replaceAll("'","%27%27")
                .replaceAll("\\(","%28")
                .replaceAll("\\)","%29")
                .replaceAll("\\*","%2A")
                .replaceAll("\\+","%2B")
                .replaceAll(",","%2C")
                .replaceAll("-","%2D")
                .replaceAll("/","%2F")
                .replaceAll(":","%3A")
                .replaceAll(";","%3B")
                .replaceAll("<","%3C")
                .replaceAll("=","%3D")
                .replaceAll(">","%3F")
                .replaceAll("\\?","%40")
                .replaceAll("@","%5B")
                .replaceAll("\\[","%5C")
                //.replaceAll("\","%5D") ?
                .replaceAll("]","%7B")
                .replaceAll("\\{","%7C")
                .replaceAll("}","%7D");
    }

    private HashMap<String,Integer> createHashMap(HttpResponse<String> string){
        String substringCast = string.body().substring(1,string.body().length()-1);
        substringCast = substringCast.replaceAll("\"","" );
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        String[] pairsCast = substringCast.split(",");
        for(int i=0; i<pairsCast.length; i++) {
            String pair = pairsCast[i];
            String[] keyValue = pair.split(":");
            hashMap.put(keyValue[0], Integer.valueOf(keyValue[1]));
        }
        return hashMap;
    }

    public HashMap<String, Integer> sortByValueDesc(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }



}*/
