package com.sep.server.api;

import com.sep.server.model.User;
import com.sep.server.model.UserProfile;
import com.sep.server.services.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;

@RestController
public class UserProfileRestController {

    private UserProfileService userProfileService;

    public UserProfileRestController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping(path = "usersProfile/friends")
    public ResponseEntity<String> getFriendsFromUsername(@RequestParam(required = false,value = "search") String username) {
        return userProfileService.getFriends(username);
    }

    @GetMapping(path = "usersProfile/watchlist")
    public ResponseEntity<String> getWatchlistFromUsername(@RequestParam(required = false,value = "search") String username) {
        return userProfileService.getWatchlist(username);
    }

    @GetMapping(path = "usersProfile/alreadyWatchedlist")
    public ResponseEntity<String> getWatchedlistFromUsername(@RequestParam(required = false,value = "search") String username) {
        return userProfileService.getWatchedlist(username);
    }

    @GetMapping(path = "usersProfile/getRecommendation")
    public ResponseEntity<String> getRecommendationFromUsername(@RequestParam(required = false,value = "search") String username) {
        return userProfileService.getMovieRecommendation(username, false);
    }

    @GetMapping(path = "usersProfile/getFriendsRecommendation")
    public ResponseEntity<String> getFriendsRecommendationFromUsername(@RequestParam(required = false,value = "search") String username) {
        return userProfileService.getMovieRecommendation(username, true);
    }


    @GetMapping(path="usersProfile/getGenre")
    public ResponseEntity<HashMap> getGenre(@RequestParam(required = false, value = "username") String username,
                                            @RequestParam(required = false, value = "startDate") String startDate,
                                            @RequestParam(required = false, value = "endDate") String endDate) {
        return userProfileService.getGenre(username, startDate, endDate);
    }

    @GetMapping(path="usersProfile/getCast")
    public ResponseEntity<HashMap> getCast(@RequestParam(required = false, value = "username") String username,
                                            @RequestParam(required = false, value = "startDate") String startDate,
                                            @RequestParam(required = false, value = "endDate") String endDate) {
        return userProfileService.getCast(username, startDate, endDate);
    }

    @GetMapping(path="usersProfile/getFilmlengthStat")
    public ResponseEntity<String> getFilmlengthStat(@RequestParam(required = false, value = "username") String username,
                                           @RequestParam(required = false, value = "startDate") String startDate,
                                           @RequestParam(required = false, value = "endDate") String endDate) {
        return userProfileService.getFilmlengthStat(username, startDate, endDate);
    }

    @GetMapping(path = "usersProfile/getFavoriteMovie")
    public ResponseEntity<String> getFavoriteMovie(@RequestParam(required = false, value = "search") String username){
        return userProfileService.getFavoriteMovie(username);
    }

    @PostMapping(path = "usersProfile/addFriends")
    public ResponseEntity<String> addFriendsFromUsername(@RequestParam(value="username") String username,@RequestParam(value="newFriend") String newFriend) {
        return userProfileService.addFriend(username,newFriend);
    }

    @PostMapping(path ="usersProfile/create")
    public ResponseEntity<String> setUserProfile(@RequestBody
    UserProfile user) {
        return userProfileService.createUserProfile(user);
    }

    @PostMapping(path = "usersProfile/addStat")
    public ResponseEntity<String> setFilter(@RequestParam(required = false, value = "username") String username,
                                            @RequestParam(required = false, value = "moviename") String moviename,
                                            @RequestParam(required = false, value = "filmlength") String filmlength,
                                            @RequestParam(required = false, value = "date") String date,
                                            @RequestParam(required = false, value = "cast") String cast,
                                            @RequestParam(required = false, value = "category") String category)
    {return userProfileService.addStat(username, moviename, filmlength, date, cast, category);}

    @PostMapping(path = "usersProfile/setFavoriteMovie")
    public ResponseEntity<String> setFavoriteMovie(@RequestParam(required = false, value = "search") String username, @RequestBody String moviename){
        return userProfileService.setFavoriteMovie(username, moviename);
    }
}
