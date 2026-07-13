package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.Encryption;
import com.sep.client.extras.HttpRequests;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

//JavaFX Controller klasse für das Register Fenster
public class RegisterController {

    @FXML
    private Button continueButton, backToLoginButton, pfpButton;

    @FXML
    private Label emailTakenError, nameErrorLabel, passwordErrorLabel, usernameTakenLabel,dateErrorLabel, adminBestätigungLabel;

    @FXML
    private PasswordField setPasswordfield, confirmPasswordfield;

    @FXML
    private CheckBox showPasswordCheckBox, registerAsAdminCheckbox;

    @FXML
    private TextField emailTextfield, lastnameTextfield, usernameTextfield, firstnameTextfield, showPasswordTextfield,showConfirmPasswordTextfield;

    @FXML
    private DatePicker dateOfBirthDatePicker;

    @FXML
    private ImageView profilePictureImageView;

    private boolean errorAlertExecuted = false;

    private File myPFP;
    private Image image;
    private String baseImage;



    //Wichtige Anmerkungen im vorraus: initialize sorgt, dass die Methoden mit Listener am Anfang gestartet werden.
    //Methoden die ein addListener und ein Override der Methode changed() haben, reagieren in Echtzeit auf Nutzereingaben
    public void initialize(){
        baseImage= HttpRequests.getString("noPFP","/users/pfp");
        ShowDataController.showBaseImage("noPFP","/users/pfp",profilePictureImageView);
        checkNameFormat(firstnameTextfield,lastnameTextfield);
        checkNameFormat(lastnameTextfield,firstnameTextfield);
        toggleVisiblePassword(null);
        checkEmailAvailabilityAndRights(emailTextfield);
        checkUsernameAvailability(usernameTextfield);
        //jedes Feld muss einmal vorne sein
        checkPassword(confirmPasswordfield,setPasswordfield);
        checkPassword(setPasswordfield,confirmPasswordfield);
        checkPassword(showPasswordTextfield,showConfirmPasswordTextfield);
        checkPassword(showConfirmPasswordTextfield,showPasswordTextfield);

    }

    @FXML
    public void onContinueButtonEvent(ActionEvent event) {
        errorAlert();
            //checkt ob eine Fehlermeldung erschienen ist
            if (!errorAlertExecuted) {

                String firstname = firstnameTextfield.getText();
                String lastname  = lastnameTextfield.getText();
                String username  = usernameTextfield.getText().replaceAll("\\s","_");
                String email     = emailTextfield.getText();
                String password  = setPasswordfield.getText();
                String format = "yyyy-MM-dd";
                DateTimeFormatter formatter= DateTimeFormatter.ofPattern(format);

                if (registerAsAdminCheckbox.isSelected()) {
                   LocalDate dateOfBirth = null;

                   registerUser(firstname, lastname, username, email, password, null,true, true);
                }
                 else {
                     if(dateOfBirthDatePicker.getValue()==null){
                         dateErrorLabel.setText("Bitte ein Datum auswählen");
                         errorAlert();
                         dateErrorLabel.setText("");
                     }else{
                         dateOfBirthDatePicker.setValue(dateOfBirthDatePicker.getConverter().fromString(dateOfBirthDatePicker.getEditor().getText()));
                         LocalDate dateOfBirth = dateOfBirthDatePicker.getValue();
                         registerUser(firstname, lastname, username, email, password, dateOfBirth,false, true);
                         try {
                             //Sendet den encodierten Base64-String/Profilbild
                             HttpRequests.postString("/users/choosePFP",username,baseImage);
                         } catch (IOException e) {
                             e.printStackTrace();
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         Alerts.successAlert(continueButton);
                     }

                }


            }
            //setzt den bool-wert wieder zurück, so das man die Daten korrigieren kann
            else {
                errorAlertExecuted=false;
            }
    }

    @FXML
    public void onBackToLoginButtonEvent(ActionEvent event) {
        ((Stage) backToLoginButton.getScene().getWindow()).close();
    }
    //lädt ein Bild auf die Festplatte
    @FXML
    private  void onUploadPfpButtonEvent(ActionEvent event){
        baseImage=ShowDataController.chooseImage(myPFP,image,baseImage,profilePictureImageView);
    }

    @FXML
    public void toggleVisiblePassword(ActionEvent event) {
        //wenn Haken bei der Box, werden die Passwortfelder mit normalen Felder getauscht
        if (showPasswordCheckBox.isSelected()) {
            showPasswordTextfield.setText(setPasswordfield.getText());
            showPasswordTextfield.setVisible(true);
            setPasswordfield.setVisible(false);
            //nochmal für das zweite Passwortfeld
            showConfirmPasswordTextfield.setText(confirmPasswordfield.getText());
            showConfirmPasswordTextfield.setVisible(true);
            confirmPasswordfield.setVisible(false);
            return;
        }
        //wenn kein Haken bei der Box, werden die normalen Felder mit den Passwortfeldern getauscht
        setPasswordfield.setText(showPasswordTextfield.getText());
        setPasswordfield.setVisible(true);
        showPasswordTextfield.setVisible(false);
        //nochmal für das zweite Passwortfeld
        confirmPasswordfield.setText(showConfirmPasswordTextfield.getText());
        confirmPasswordfield.setVisible(true);
        showConfirmPasswordTextfield.setVisible(false);
    }

    public void registerUser(String firstname, String lastname, String username, String email, String password, LocalDate dateOfBirth, Boolean isAdmin, boolean hasTwoFA) {
        try {
            HttpRequests.post("/users/create", HttpRequests.createUserJSONObject(firstname, lastname, username, email, Encryption.encryptString(password), dateOfBirth, isAdmin, hasTwoFA));
            HttpRequests.createPrivacySettings(username);
            createUserProfile(username);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void createUserProfile(String username){
        try {
            HttpRequests.post("/usersProfile/create",HttpRequests.createUserProfileJSONObject(username,null,null,null));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void errorAlert() {
        //wenn Felder leer sind
        if (isEmptyField(firstnameTextfield)||isEmptyField(lastnameTextfield)||isEmptyField(usernameTextfield)||isEmptyField(emailTextfield)||(isEmptyPasswordfield(setPasswordfield)&&isEmptyField(showPasswordTextfield))||(isEmptyPasswordfield(confirmPasswordfield)&&isEmptyField(showConfirmPasswordTextfield))){
            errorAlertExecuted = true;
            Alerts.emptyFieldsAlert();
            //wenn Fehlermeldungen existieren
        } else if (checkErrormessage(usernameTakenLabel)||checkErrormessage(emailTakenError)||checkErrormessage(nameErrorLabel)||checkErrormessage(passwordErrorLabel)||checkErrormessage(usernameTakenLabel)||checkErrormessage(dateErrorLabel)){
            errorAlertExecuted = true;
            Alerts.errorMessagesOpenAlert();
        }
    }

    private boolean isEmptyField(TextField field) {
        //prüft ob die Textfelder leer sind
        return field.getText().isBlank();
    }

    private boolean isEmptyPasswordfield(PasswordField field) {
        //prüft ob die Passwortfelder leer sind
        return field.getText().equals("");
    }

    private boolean checkErrormessage(Label errormessage) {
        //prüft ob es noch Fehlermeldungen existieren
        return !Objects.equals(errormessage.getText(), "");
    }


    private void checkUsernameAvailability(TextField name) {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //Prüft durch Server-Abfrage ob der Nutzername schon existiert
                if (HttpRequests.check(name.getText().replaceAll("\\s","_"), "/users/findUsername")||name.getText().equals("noPFP")) {
                    usernameTakenLabel.setText("Der Benutzernamen ist vergeben");
                } else {
                    usernameTakenLabel.setText("");
                }
            }
        });
    }

    private void checkEmailAvailabilityAndRights(TextField name) {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //prüft ob es eine gültige Email ist
                String pattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
                if (!Pattern.compile(pattern).matcher(name.getText()).matches()) {
                    emailTakenError.setText("Dies ist keine gültige E-Mail Adresse !");
                }
                //Prüft durch Server-Abfrage ob die Email schon existiert
               else if (HttpRequests.check(name.getText(),"/users/findEmail")) {
                    emailTakenError.setText("Die E-Mail-Adresse wird bereits verwendet !");
               } else {
                   emailTakenError.setText("");
               }
                //Prüft durch Server-Abfrage ob die Email auf der Whitelist steht/ ob sich ein Admin registriert
            }
        });
    }

    private void checkPassword(TextField erstesFeld, TextField zweitesFeld){
        //vergleicht die ganzen Passwortfelder, dabei wird in der Methode intialize jedes Feld einen Listener hinzugefügt
        erstesFeld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //vergleicht beide Passwörter
                if (zweitesFeld.getText().equals(erstesFeld.getText())||zweitesFeld.getText().isEmpty()) {
                    passwordErrorLabel.setText("");
                } else {
                    passwordErrorLabel.setText("Keine Übereinstimmung!");
                }

            }
        });
    }

    private void checkNameFormat(TextField name,TextField name2) {
        name.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //guck ob der Name keine Zahlen beinhaltet
                if (!name.getText().matches("^[a-zA-Z\\s]*$")||!name2.getText().matches("^[a-zA-Z\\s]*$+")) {
                    nameErrorLabel.setText("Haben Sie Ihren Namen richtig eingegeben?");
                } else {
                    nameErrorLabel.setText("");
                }
            }
        });
    }






}

