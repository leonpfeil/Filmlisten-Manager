/*package com.sep.server;

import com.sep.server.dbaccess.MovieRepository;
import com.sep.server.dbaccess.RateMovieRepository;
import com.sep.server.dbaccess.UserProfileRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.*;
import com.sep.server.services.MovieService;
import com.sep.server.services.UserProfileService;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MovieRecTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    RateMovieRepository rateMovieRepository;
    @Autowired
    MovieService movieService;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    MovieRepository movieRepository;

    public void initialize(){

        //Erstellen User
        User user1= createUser("A");
        User user2= createUser("B");
        User user3= createUser("C");

        //Erstellen userProfile
        UserProfile userProfile1 = createUserProfile(user1);
        UserProfile userProfile2 = createUserProfile(user2);
        UserProfile userProfile3 = createUserProfile(user3);

        //Erstellen Filme mit Kategorien
        for(int i=0; i<20;i++){
            Movie movie = new Movie();
            movie.setMovieName(String.valueOf(i));
            movie.setCategory(MovieCategory.ACTION.category());
            movieRepository.save(movie);
        }
        for(int i=20; i<40;i++){
            Movie movie = new Movie();
            movie.setMovieName(String.valueOf(i));
            movie.setCategory(MovieCategory.ADVENTURE.category());
            movieRepository.save(movie);
        }
        for(int i=40; i<60;i++){
            Movie movie = new Movie();
            movie.setMovieName(String.valueOf(i));
            movie.setCategory(MovieCategory.FANTASY.category());
            movieRepository.save(movie);
        }
        for(int i=60; i<80;i++){
            Movie movie = new Movie();
            movie.setMovieName(String.valueOf(i));
            movie.setCategory(MovieCategory.ACTION.category()+", "+MovieCategory.FANTASY.category());
            movieRepository.save(movie);
        }
        for(int i=80; i<100;i++){
            Movie movie = new Movie();
            movie.setMovieName(String.valueOf(i));
            movie.setCategory(MovieCategory.COMEDY.category()+", "+MovieCategory.MUSIC);
            movieRepository.save(movie);
        }
        for(int i=100; i<120;i++){
            Movie movie = new Movie();
            movie.setMovieName(String.valueOf(i));
            movie.setCategory(MovieCategory.HORROR.category());
            movieRepository.save(movie);
        }

        //Erstellen WatchedList
        userProfile1.setWatchedlist("40,#,41,#,42,#,43,#,44,#,20,#,21,#,22,#,23,#,24");
        userProfile2.setWatchedlist("50,#,51,#,52,#,53,#,54,#,55,#,56,#,57,#,0,#,1,#,2,#,3,#,4");
        userProfile3.setWatchedlist("30,#,31,#,20,#,21,#,40,#,41,#,42,#,43,#,44");

        //Freunde setzen
        userProfile1.setFriendslist("TestUserB,#,TestUserC");
        userProfile2.setFriendslist("TestUserA,#,TestUserC");
        userProfile3.setFriendslist("TestUserA,#,TestUserB");

        //Erstellen UserProfiles
        userProfileRepository.save(userProfile1);
        userProfileRepository.save(userProfile2);
        userProfileRepository.save(userProfile3);

        //Erstellen Ratings
        rateMovieRepository.save(createMovieRating("TestUserA","40",5));
        rateMovieRepository.save(createMovieRating("TestUserA","41",4));
        rateMovieRepository.save(createMovieRating("TestUserA","42",3));
        rateMovieRepository.save(createMovieRating("TestUserA","43",2));
        rateMovieRepository.save(createMovieRating("TestUserA","44",5));

        rateMovieRepository.save(createMovieRating("TestUserC","47",5));
        rateMovieRepository.save(createMovieRating("TestUserC","46",4));
        rateMovieRepository.save(createMovieRating("TestUserC","45",3));
        rateMovieRepository.save(createMovieRating("TestUserC","44",2));
        rateMovieRepository.save(createMovieRating("TestUserC","43",1));

        rateMovieRepository.save(createMovieRating("TestUserB","4",5));
        rateMovieRepository.save(createMovieRating("TestUserB","3",4));
        rateMovieRepository.save(createMovieRating("TestUserB","2",3));
        rateMovieRepository.save(createMovieRating("TestUserB","1",2));
        rateMovieRepository.save(createMovieRating("TestUserB","0",1));

        rateMovieRepository.save(createMovieRating("TestUserB","50",5));
        rateMovieRepository.save(createMovieRating("TestUserB","51",1));
        rateMovieRepository.save(createMovieRating("TestUserB","52",5));
        rateMovieRepository.save(createMovieRating("TestUserB","53",1));
        rateMovieRepository.save(createMovieRating("TestUserB","54",5));

        for(int i=0;i<120;i++){
            Movie movie = new Movie();
            movie.setMovieName(String.valueOf(i));
            movieService.setGlobalRating(movie);
        }
    }

    @Test
    public void MovieRecTest(){

        initialize();

        List<String> movieRecs= new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/usersProfile/getRecommendation?search=TestUserC"))
                    .GET()
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String repString = response.body().toString();
            JSONArray movieRecsArray = new JSONArray(repString);

            for (int i = 0; i< movieRecsArray.length(); i++) {
                movieRecs.add(movieRecsArray.getString(i));
            }

        }  catch (Exception e) {
            e.printStackTrace();
        }



        List<String> friendsMovieRecs= new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/usersProfile/getFriendsRecommendation?search=TestUserC"))
                    .GET()
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String repString = response.body().toString();
            JSONArray movieRecsArray = new JSONArray(repString);

            for (int i = 0; i< movieRecsArray.length(); i++) {
                friendsMovieRecs.add(movieRecsArray.getString(i));
            }

        }  catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Anzhal Filme (Empfehlung basierend auf WatchedList)", 15, movieRecs.size());
        assertEquals("Anzhal Filme (Empfehlung basierend auf Freunden)", 15, friendsMovieRecs.size());

        List<String> watchedList= Stream.of(("30,#,31,#,20,#,21,#,40,#,41,#,42,#,43,#,44".split(",#,"))).collect(Collectors.toList());

        List<String> notSupposed1 = new ArrayList<>();
        List<String> notSupposed2 = new ArrayList<>();

        for(String w : watchedList){
            for (String m : movieRecs){
                if(m.equals(w)) {
                    notSupposed1.add(m);
                }
            }
            for (String m : friendsMovieRecs){
                if(m.equals(w)) {
                    notSupposed1.add(m);
                }
            }
        }

        assertEquals("Filme aus WatchedList in Recs:" , 0 , notSupposed1.size());
        assertEquals("Filme aus WatchedList in FriendRecs:" , 0 , notSupposed2.size());
        //"44, 40, 4, 50, 52, 54" 5 Sterne-Bewertungen der Freunde
        //"30,#,31,#,20,#,21,#,40,#,41,#,42,#,43,#,44" watchedlist

        assertEquals("Film 4 in Liste", true, inList("4",friendsMovieRecs));
        assertEquals("Film 50 in Liste", true, inList("50",friendsMovieRecs));
        assertEquals("Film 52 in Liste", true, inList("52",friendsMovieRecs));
        assertEquals("Film 54 in Liste", true, inList("54",friendsMovieRecs));

        assertEquals("Film 44 in Liste", false, inList("44",friendsMovieRecs));
        assertEquals("Film 40 in Liste", false, inList("40",friendsMovieRecs));

        deleteMovies();
        deleteUserProfile();
        deleteUser();
        deleteMovieRating();
    }

    public boolean inList(String movieName, List<String> movieList){
        for(String m : movieList){
            if (m.equals(movieName)){
                return true;
            }
        }
        return false;
    }

    public void cleanUp(){
        deleteMovies();
        deleteUserProfile();
        deleteUser();
        deleteMovieRating();
    }


    public void deleteUser(){
        userRepository.deleteById("TestUserA");
        userRepository.deleteById("TestUserB");
        userRepository.deleteById("TestUserC");
    }

    public void deleteMovies(){
        for(int i =0; i<120;i++){
            movieRepository.deleteById(String.valueOf(i));
        }
    }

    public void deleteUserProfile(){
        userProfileRepository.deleteById("TestUserA");
        userProfileRepository.deleteById("TestUserB");
        userProfileRepository.deleteById("TestUserC");
    }

    public void deleteMovieRating(){
        List<String> user = new ArrayList<>();
        user.add("TestUserA");
        user.add("TestUserB");
        user.add("TestUserC");

        for(int i = 0; i<60;i++) {
            for (String m : user) {
                try {
                    rateMovieRepository.deleteById(m + i);
                }
                catch (Exception e){
                }
            }
        }
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

    private UserProfile createUserProfile(User a){
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(a);
        return userProfile;
    }

    private RateMovie createMovieRating(String username, String movieName, int rating){
        RateMovie rateMovie = new RateMovie();
        rateMovie.setRatingname(username+movieName);
        rateMovie.setMovieName(movieName);
        rateMovie.setUsername(username);
        rateMovie.setRating(rating);

        return rateMovie;
    }
}*/