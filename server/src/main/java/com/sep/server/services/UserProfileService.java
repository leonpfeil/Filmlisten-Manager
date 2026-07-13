package com.sep.server.services;

import com.mysql.cj.xdevapi.JsonArray;
import com.sep.server.dbaccess.RateMovieRepository;
import com.sep.server.dbaccess.UserProfileRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.MovieCategory;
import com.sep.server.model.RateMovie;
import com.sep.server.model.User;
import com.sep.server.model.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository ;
    private final RateMovieRepository rateMovieRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository, RateMovieRepository rateMovieRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.rateMovieRepository = rateMovieRepository;
    }

    public ResponseEntity<String> getFriends(String username) {
        try{
            UserProfile userProfile = userProfileRepository.getUserProfileByUsername(username);
            String friends="";
            if(userProfile !=null && userProfile.getFriendslist()!=null) {
                 friends = userProfile.getFriendslist();
                return new ResponseEntity(friends, HttpStatus.OK);
            } else{
                return new ResponseEntity(null, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<String> getWatchlist(String username) {
        try{
            UserProfile userProfile = userProfileRepository.getUserProfileByUsername(username);
            String watchlist="";
            if(userProfile.getWatchlist()!=null) {
                watchlist = userProfile.getWatchlist();
                return new ResponseEntity(watchlist, HttpStatus.OK);
            }else{
                return new ResponseEntity(null, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<String> getWatchedlist(String username) {
        try{
            UserProfile userProfile = userProfileRepository.getUserProfileByUsername(username);
            String watchedList="";
            if(userProfile.getWatchedlist()!=null) {
                 watchedList = userProfile.getWatchedlist();
                return new ResponseEntity(watchedList, HttpStatus.OK);
            }
            else{
                return new ResponseEntity(null, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<String> createUserProfile(UserProfile userProfile) {
        User user = userRepository.getUserByUsername(userProfile.getUsername());
        userProfile.setUser(user);
        userProfileRepository.save(userProfile);

        String response ="created: "+userProfile.toString();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    public ResponseEntity<String> addFriend(String username,String newFriend) {
        try{
            UserProfile userProfile = userProfileRepository.getUserProfileByUsername(username);
            if(userProfile.getFriendslist() != null)
            {
                userProfile.setFriendslist(userProfile.getFriendslist() + ",#," + newFriend);
            }
            else
            {
                userProfile.setFriendslist(newFriend);
            }
            userProfileRepository.save(userProfile);

            //Same Process but for the other user
            userProfile = userProfileRepository.getUserProfileByUsername(newFriend);
            if(userProfile.getFriendslist() != null)
            {
                userProfile.setFriendslist(userProfile.getFriendslist() + ",#," + username);
            }
            else
            {
                userProfile.setFriendslist(username);
            }
            userProfileRepository.save(userProfile);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<String> getMovieRecommendation(String username, Boolean bool) {
        //sucht die Liste der gesehenen Filme

        UserProfile user = userProfileRepository.getUserProfileByUsername(username);
        List<String> listOfElementsNames= new ArrayList<>();
        List<String> bestMovies = new ArrayList<>();
        List<String> fiveStarRatedMovies = new ArrayList<>();


        System.out.println(bool);
        if(bool){
            String friends= user.getFriendslist();

            //Abbruch wenn keine Freunde
            if(friends==null) {
                return new ResponseEntity("", HttpStatus.NO_CONTENT);
            }

            List<String> friendsList = Stream.of((friends.split(",#,"))).collect(Collectors.toList());
            for (int i = 0; i < friendsList.size(); i++) {
                UserProfile userProfile = userProfileRepository.getUserProfileByUsername(friendsList.get(i));
                listOfElementsNames.addAll(lastTenMovies(userProfile));

                List<String> fiveRatedOfUser = rateMovieRepository.getAllByUsernameEqualsAndRatingEquals(friendsList.get(i), 5)
                        .stream()
                        .map(RateMovie::getMovieName)
                        .collect(Collectors.toList());

                fiveStarRatedMovies.addAll(fiveRatedOfUser);
            }

        }else{
            listOfElementsNames=lastTenMovies(user);

            //Abbruch wenn keine Watchedlist
            if(listOfElementsNames.size()==0) {
                return new ResponseEntity("", HttpStatus.NO_CONTENT);
            }
        }


        //sucht die Kategorie, welche die meisten Filme haben
        int max=0;
        String favouriteCategory="";
        for(MovieCategory mc : MovieCategory.values()){
            int counter=0;

            for(int i=0;i<listOfElementsNames.size();i++){

                Boolean checkCategory = MovieService.getMovie(listOfElementsNames.get(i)).getCategory().contains(mc.category());
                if(checkCategory){
                    counter++;
                }
            }
            if(counter>max){
                max=counter;
                favouriteCategory=mc.category();
            }
        }

        listOfElementsNames=getListofWatchedList(user);
        bestMovies = MovieService.getBestRatedMoviesOfCategory(favouriteCategory,listOfElementsNames);

        //Senden der eigenen Liste
        if(!bool){return new ResponseEntity(bestMovies, HttpStatus.OK);}

        //Zusammenfügen von fiveRated und bestMovies (bei Freunden)

        List<String> fiveStarsFiltered =filterListWithMax(fiveStarRatedMovies,listOfElementsNames,15);
        List<String> bestFiltered = filterListWithMax(bestMovies, fiveStarsFiltered, 15-fiveStarsFiltered.size());
        fiveStarsFiltered.addAll(bestFiltered);

        return new ResponseEntity(fiveStarsFiltered, HttpStatus.OK);
    }


    public List<String> getListofWatchedList(UserProfile userProfile){
        List<String> watchedList= new ArrayList<>();
        if(userProfile.getWatchedlist()!=null) {
            String watchedListe = userProfile.getWatchedlist();
            watchedList= Stream.of((watchedListe.split(",#,"))).collect(Collectors.toList());
        }
        return  watchedList;
    }
    public List<String> lastTenMovies(UserProfile userProfile){
        List<String> tenMovies= getListofWatchedList(userProfile);
        //letze 10 Filme
        int Quantity=tenMovies.size();
        if(Quantity>10){
            int difference=Quantity-10;
            for (int i=0;i<difference;i++){
                tenMovies.remove(0);
            }
        }
        return tenMovies;
    }

 public ResponseEntity<String> addStat(String username, String moviename, String filmlength, String date, String cast, String category) {
     try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
         PreparedStatement addStat = con.prepareStatement("INSERT INTO `user_statistic`(`cast`,`date`,`genre`,`movie_name`,`movie_length`,`username`) VALUES ('"+cast+"','"+date+"','"+category+"','"+moviename+"','"+filmlength+"','"+username+"')");
         addStat.execute();
     } catch (SQLException e) {
         throw new RuntimeException(e);
     }
     return null;
 }

    public ResponseEntity<HashMap> getGenre(String username, String startDate, String endDate) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement statsPS = con.prepareStatement("Select * FROM user_statistic WHERE username='"+username+"'");
            ResultSet statsRS = statsPS.executeQuery();

            HashMap<String, Integer> stats = new HashMap<String, Integer>();
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);

            while(statsRS.next()) {
                if(statsRS.getDate("date").after(start) && statsRS.getDate("date").before(end)) {
                    List<String> genreList = new ArrayList<String>(Arrays.asList(statsRS.getString("genre").split(", ")));
                    for(String genre : genreList) {
                        if(stats.containsKey(genre)) {
                            stats.put(genre, stats.get(genre)+1);
                        }
                        else {
                            stats.put(genre, 1);
                        }
                    }
                }
            }
            return new ResponseEntity(stats, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<HashMap> getCast(String username, String startDate, String endDate) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement statsPS = con.prepareStatement("Select * FROM user_statistic WHERE username='"+username+"'");
            ResultSet statsRS = statsPS.executeQuery();

            HashMap<String, Integer> stats = new HashMap<String, Integer>();
            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);

            while(statsRS.next()) {
                if(statsRS.getDate("date").after(start) && statsRS.getDate("date").before(end)) {
                    List<String> castList = new ArrayList<String>(Arrays.asList(statsRS.getString("cast").split(",")));
                    for(String cast : castList) {
                        if(stats.containsKey(cast)) {
                            stats.put(cast, stats.get(cast)+1);
                        }
                        else {
                            stats.put(cast, 1);
                        }
                    }
                }
            }
            return new ResponseEntity(stats, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> getFilmlengthStat(String username, String startDate, String endDate) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement statsPS = con.prepareStatement("Select movie_length, date FROM user_statistic WHERE username='"+username+"'");
            ResultSet statsRS = statsPS.executeQuery();

            Date start = Date.valueOf(startDate);
            Date end = Date.valueOf(endDate);
            Integer filmlength = 0;

            while(statsRS.next()) {
                if(statsRS.getDate("date").after(start) && statsRS.getDate("date").before(end)) {
                    filmlength = filmlength + statsRS.getInt("movie_length");
                }
            }

            String filmlengthString = filmlength.toString();
            return new ResponseEntity(filmlengthString, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> filterListWithMax(List<String> list, List<String> filter, int max){

        int MovieCounter=0;
        List<String> filteredList = new ArrayList<>();

        for (String m : list){
            //Film auf WatchedList?
            boolean add=true;
            for(String w : filter){
                if(m.equals(w)){
                    add = false;
                    break;
                }
            }

            //ggf Film hinzufügen
            if(add){
                filteredList.add(m);
                MovieCounter++;
            }

            //Test ob Liste bereits max ist
            if(MovieCounter==max){
                return filteredList;
            }
        }
       return filteredList;
    }

    public ResponseEntity<String> setFavoriteMovie (String username, String moviename) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("UPDATE `user_profile` SET `favorite_movie`='"+moviename+"' WHERE user_username='"+username+"'");
            state.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public ResponseEntity<String> getFavoriteMovie(String username) {
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("SELECT favorite_movie FROM user_profile WHERE user_username='"+username+"'");
            ResultSet rs = state.executeQuery();
            rs.next();
            String favMovie = rs.getString("favorite_movie");
            return new ResponseEntity(favMovie, HttpStatus.OK);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

