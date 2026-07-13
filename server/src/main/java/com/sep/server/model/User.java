package com.sep.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class User {
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @Id
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("dateOfBirth")
    @Column(columnDefinition = "Date")
    private Date dateOfBirth;
    @JsonProperty("isAdmin")
    private Boolean isAdmin;
    @JsonProperty("pfpImagePath")
    private String pfpImagePath;

    @JsonProperty("hasTwoFA")
    private Boolean twoFA;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserProfile userprofile;


    public boolean getTwoFA() {
        return twoFA;
    }

    public void setTwoFA(boolean twoFA) {
        this.twoFA = twoFA;
    }

    public String getPfpImagePath() {
        return pfpImagePath;
    }

    public void setPfpImagePath(String pfpImagePath) {
        this.pfpImagePath = pfpImagePath;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
