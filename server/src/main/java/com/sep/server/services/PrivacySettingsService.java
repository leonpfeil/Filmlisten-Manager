package com.sep.server.services;

import com.sep.server.dbaccess.PrivacySettingsRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.PrivacySettings;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;


@Service
public class PrivacySettingsService {
    private PrivacySettingsRepository privacySettingsRepository;

    public PrivacySettingsService(PrivacySettingsRepository privacySettingsRepository) {
        this.privacySettingsRepository = privacySettingsRepository;
    }

    public void createPrivacySettings(String username) {
        PrivacySettings privacySettings = new PrivacySettings();

        privacySettings.setUsername(username);
        privacySettings.setFriendsList("0");
        privacySettings.setReviews("0");
        privacySettings.setWatchedList("0");
        privacySettings.setWatchList("0");

        privacySettingsRepository.save(privacySettings);
    }
    //Methoden um den Privacy Status für die Listen rauszufinden
    public ResponseEntity<String> getFriendListPrivacyStatus(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        String friendList = privacySettings.getFriendsList();
        return new ResponseEntity(friendList, HttpStatus.OK);
    }

    public ResponseEntity<String> getReviewListPrivacyStatus(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        String reviewList = privacySettings.getReviews();
        return new ResponseEntity(reviewList, HttpStatus.OK);
    }

    public ResponseEntity<String> getWatchedListPrivacyStatus(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        String watchedList = privacySettings.getWatchedList();
        return new ResponseEntity(watchedList, HttpStatus.OK);
    }

    public ResponseEntity<String> getWatchListPrivacyStatus(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        String watchList = privacySettings.getWatchList();
        return new ResponseEntity(watchList, HttpStatus.OK);
    }

    //Friendlist
    public void setFriendListPublic(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setFriendsList("0");
        privacySettingsRepository.save(privacySettings);
    }

    public void setFriendListOnlyFans(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setFriendsList("1");
        privacySettingsRepository.save(privacySettings);
    }

    public void setFriendListPrivate(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setFriendsList("2");
        privacySettingsRepository.save(privacySettings);
    }

    //Watchedlist
    public void setWatchedListPublic(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setWatchedList("0");
        privacySettingsRepository.save(privacySettings);
    }

    public void setWatchedListOnlyFans(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setWatchedList("1");
        privacySettingsRepository.save(privacySettings);
    }

    public void setWatchedListPrivate(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setWatchedList("2");
        privacySettingsRepository.save(privacySettings);
    }

    //Watchlist
    public void setWatchListPublic(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setWatchList("0");
        privacySettingsRepository.save(privacySettings);
    }

    public void setWatchListOnlyFans(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setWatchList("1");
        privacySettingsRepository.save(privacySettings);
    }

    public void setWatchListPrivate(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setWatchList("2");
        privacySettingsRepository.save(privacySettings);
    }

    //Reviewlist
    public void setReviewListPublic(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setReviews("0");
        privacySettingsRepository.save(privacySettings);
    }

    public void setReviewListOnlyFans(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setReviews("1");
        privacySettingsRepository.save(privacySettings);
    }

    public void setReviewListPrivate(String username) {
        PrivacySettings privacySettings = privacySettingsRepository.getById(username);

        privacySettings.setReviews("2");
        privacySettingsRepository.save(privacySettings);
    }
}
