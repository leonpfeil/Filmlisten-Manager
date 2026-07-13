/*package com.sep.server;


import com.mysql.cj.xdevapi.JsonArray;
import com.sep.server.dbaccess.MovieRepository;
import com.sep.server.dbaccess.RateMovieRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.Movie;
import com.sep.server.model.RateMovie;
import com.sep.server.model.User;
import com.sep.server.services.MovieService;
import com.sep.server.services.RateMovieService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GlobalRatingTests {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieService movieService;

    @Autowired
    RateMovieService rateMovieService;

    @Autowired
    RateMovieRepository rateMovieRepository;

    //Erstellen eines Films um zubewerten
    @Test
    public void testCreateMovie(){
        Movie movie=new Movie();
        movie.setAuthor("Test");
        movie.setMovieName("TestMovieForRating");
        movie.setBannerPath(null);
        movie.setCast("Test");
        movie.setCategory("Test");
        movie.setLength(123);
        movie.setDirector("Test");
        movie.setReleaseYear(2022);
        movie.setGlobalrating(0);
        movieRepository.save(movie);
        assertEquals("Test des Films",true,movieRepository.existsByMovieName("TestMovieForRating"));
    }
    //Nutzer die Bewerten
    @Test
    public void createUsers(){
        User user1= createUser("A");
        User user2= createUser("B");
        User user3= createUser("C");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        assertEquals("Erster Nutzer",true,userRepository.existsByUsername("TestUserA"));

    }

    //Anzahl der Reviews und Berechnung der globalen Bewertung
    @Test
    public void testMovieRating() throws JSONException {
       rateMovie("TestUserA",1);
       rateMovie("TestUserB",3);
       rateMovie("TestUserC",5);
       assertEquals("Anzahl an Reviews:","3",rateMovieService.countReviews("TestMovieForRating").getBody().get(0));

        JSONArray g = new JSONArray(get("TestMovieForRating","/ratemovie/getglobal").body());
        List<String> global= new ArrayList<>();
        for (int i=0;i<g.length();i++){
            global.add(g.getString(i));
        }

        int grating = 0;
        int count = 0;
        float globalrating = 0;

        try {
            for (int i = 0; i < g.length(); i++) {
                int z = Integer.parseInt(global.get(i));
                grating = grating + z;
                count++;
            }
            globalrating = 0;
            if(count != 0) {
                globalrating = (float) grating / count;
                globalrating = (float) (Math.round(globalrating*10)/10.0);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Berechnung des globalen Rating fehlgeschlagen");
        }


        JSONObject jsonO2 = new JSONObject();
        jsonO2.put("movieName", "TestMovieForRating");
        jsonO2.put("globalrating", globalrating);

        try {
            post("/movie/setglobal", jsonO2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        float i= 3;


        assertEquals("Globale Bewertung",i,movieRepository.getMovieByMovieName("TestMovieForRating").getGlobalrating());
        deleteTestObjects();
    }

    private void deleteTestObjects(){
        rateMovieRepository.delete(rateMovieRepository.getRateMovieByRatingname("TestUserATestMovieForRating"));
        rateMovieRepository.delete(rateMovieRepository.getRateMovieByRatingname("TestUserBTestMovieForRating"));
        rateMovieRepository.delete(rateMovieRepository.getRateMovieByRatingname("TestUserCTestMovieForRating"));
        userRepository.deleteById("TestUserA");
        userRepository.deleteById("TestUserB");
        userRepository.deleteById("TestUserC");
        movieRepository.deleteById("TestMovieForRating");
    }
    private User createUser(String a){
        User user=new User();
        user.setEmail("TestUser"+a+"@outlook.de");
        user.setFirstname("Max");
        user.setLastname("Mustermann");
        user.setUsername("TestUser"+a);
        user.setPassword("password");
        user.setDateOfBirth(null);
        user.setAdmin(true);
        user.setPfpImagePath(null);
        user.setTwoFA(false);
        return user;
    }

    private void rateMovie(String username,int rating){
        RateMovie rateMovie = new RateMovie();

        rateMovie.setRatingname(username+"TestMovieForRating");
        rateMovie.setUsername(username);
        rateMovie.setMovieName("TestMovieForRating");
        rateMovie.setRating(rating);
        rateMovie.setRatingCaption("test");
        rateMovie.setRatingText("test");

        rateMovieRepository.save(rateMovie);
    }

    public static HttpResponse<String> get(String string, String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+string))
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<String> post(String url, JSONObject jsonObject) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}*/

