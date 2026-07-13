package com.sep.server.api;

import com.sep.server.services.PrivacySettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivacySettingsRestController {
    private PrivacySettingsService privacySettingsService;

    public PrivacySettingsRestController(PrivacySettingsService privacySettingsService) {
        this.privacySettingsService = privacySettingsService;
    }

    @PostMapping(path = "privacy/create")
    public ResponseEntity<String> createPrivacySettings(@RequestParam(value = "username") String username) {
        privacySettingsService.createPrivacySettings(username);

        return new ResponseEntity("Success", HttpStatus.OK);
    }

    //Getter Methoden für die Listen
    @GetMapping(path = "privacy/getFriendListPrivacyStatus")
    public ResponseEntity<String> getFriendListPrivacyStatus(@RequestParam(value = "username") String username) {
        return privacySettingsService.getFriendListPrivacyStatus(username);
    }

    @GetMapping(path = "privacy/getReviewListPrivacyStatus")
    public ResponseEntity<String> getReviewListPrivacyStatus(@RequestParam(value = "username") String username) {
        return privacySettingsService.getReviewListPrivacyStatus(username);
    }

    @GetMapping(path = "privacy/getWatchedListPrivacyStatus")
    public ResponseEntity<String> getWatchedListPrivacyStatus(@RequestParam(value = "username") String username) {
        return privacySettingsService.getWatchedListPrivacyStatus(username);
    }

    @GetMapping(path = "privacy/getWatchListPrivacyStatus")
    public ResponseEntity<String> getWatchListPrivacyStatus(@RequestParam(value = "username") String username) {
        return privacySettingsService.getWatchListPrivacyStatus(username);
    }

    //Setter Methoden für Friendlist
    @PostMapping(path = "privacy/setFriendListPublic")
    public void setFriendListPublic(@RequestParam(value = "username") String username) {
        privacySettingsService.setFriendListPublic(username);
    }

    @PostMapping(path = "privacy/setFriendListOnlyFans")
    public void setFriendListOnlyFans(@RequestParam(value = "username") String username) {
        privacySettingsService.setFriendListOnlyFans(username);
    }

    @PostMapping(path = "privacy/setFriendListPrivate")
    public void setFriendListPrivate(@RequestParam(value = "username") String username) {
        privacySettingsService.setFriendListPrivate(username);
    }

    //Setter Methoden für Watchedlist

    @PostMapping(path = "privacy/setWatchedListPublic")
    public void setWatchedListPublic(@RequestParam(value = "username") String username) {
        privacySettingsService.setWatchedListPublic(username);
    }

    @PostMapping(path = "privacy/setWatchedListOnlyFans")
    public void setWatchedListOnlyFans(@RequestParam(value = "username") String username) {
        privacySettingsService.setWatchedListOnlyFans(username);
    }

    @PostMapping(path = "privacy/setWatchedListPrivate")
    public void setWatchedListPrivate(@RequestParam(value = "username") String username) {
        privacySettingsService.setWatchedListPrivate(username);
    }

    //Setter Methoden für Watchlist

    @PostMapping(path = "privacy/setWatchListPublic")
    public void setWatchListPublic(@RequestParam(value = "username") String username) {
        privacySettingsService.setWatchListPublic(username);
    }

    @PostMapping(path = "privacy/setWatchListOnlyFans")
    public void setWatchListOnlyFans(@RequestParam(value = "username") String username) {
        privacySettingsService.setWatchListOnlyFans(username);
    }

    @PostMapping(path = "privacy/setWatchListPrivate")
    public void setWatchListPrivate(@RequestParam(value = "username") String username) {
        privacySettingsService.setWatchListPrivate(username);
    }

    //Setter Methoden für Reviewlist
    @PostMapping(path = "privacy/setReviewListPublic")
    public void setReviewListPublic(@RequestParam(value = "username") String username) {
        privacySettingsService.setReviewListPublic(username);
    }

    @PostMapping(path = "privacy/setReviewListOnlyFans")
    public void setReviewListOnlyFans(@RequestParam(value = "username") String username) {
        privacySettingsService.setReviewListOnlyFans(username);
    }

    @PostMapping(path = "privacy/setReviewListPrivate")
    public void setReviewListPrivate(@RequestParam(value = "username") String username) {
        privacySettingsService.setReviewListPrivate(username);
    }
}
