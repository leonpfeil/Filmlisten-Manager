package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.Encryption;
import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//JavaFX Controller Klasse für das Login Fenster
public class LoginController {
    @FXML
    private Button doneButton, registerButton;

    @FXML
    private PasswordField passwordTextfield;

    @FXML
    private TextField usernameTextfield, showPasswordTextfield;

    @FXML
    private CheckBox showPasswordCheckbox;

    public void initialize() {
        autofillUser();

        toggleVisiblePassword(null);
    }

    @FXML
    private void toggleVisiblePassword(ActionEvent event) {
        if (showPasswordCheckbox.isSelected()) {
            showPasswordTextfield.setText(passwordTextfield.getText());
            showPasswordTextfield.setVisible(true);
            passwordTextfield.setVisible(false);
            return;
        }

        passwordTextfield.setText(showPasswordTextfield.getText());
        passwordTextfield.setVisible(true);
        showPasswordTextfield.setVisible(false);
    }

    @FXML
    public void onDoneButtonEvent(ActionEvent event) {
        startAuthentificationView();
    }

    @FXML
    public void onEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER)) {
            startAuthentificationView();
        }
    }

    private void startAuthentificationView() {
        String password  = null;

        try {
            try {
                password = Encryption.encryptString(passwordTextfield.getText());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (verifyTextFields()) {
                Alerts.giveEmptyTextfieldAlert(usernameTextfield, passwordTextfield, showPasswordTextfield);
            } else if (!HttpRequests.check(usernameTextfield.getText(), "/users/findUsername")) {
                Alerts.giveWrongUsernameAlert(usernameTextfield);
            } else if (!HttpRequests.checkPassword(usernameTextfield.getText(), password, "/users/findPasswordByUsername") && !HttpRequests.checkPassword(usernameTextfield.getText(), showPasswordTextfield.getText(), "/users/findPasswordByUsername")) {
                Alerts.giveWrongPasswordAlert(passwordTextfield);
            } else {
                if (HttpRequests.checkIfTwoFAEnabled(usernameTextfield.getText())) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/authentificationView.fxml"));
                    Parent root = (Parent) fxmlLoader.load();

                    AuthentificationController authentificationController = fxmlLoader.getController();
                    authentificationController.getEnteredUsername(usernameTextfield.getText());

                    Stage loginStage = (Stage) doneButton.getScene().getWindow();
                    Stage authentificationStage = new Stage();
                    authentificationStage.setScene(new Scene(root));
                    loginStage.close();
                    authentificationStage.show();
                } else {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/userView.fxml"));
                    Parent root = (Parent) fxmlLoader.load();

                    UserController userController = fxmlLoader.getController();
                    userController.setUsernameForOverlay(usernameTextfield.getText(), HttpRequests.checkIfUserIsAdmin("/users/isAdmin", usernameTextfield.getText()));

                    Stage loginStage = (Stage) doneButton.getScene().getWindow();
                    Stage userStage = new Stage();
                    userStage.setScene(new Scene(root));
                    loginStage.close();
                    userStage.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onRegisterButtonEvent(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/registerView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verifyTextFields() {
        return usernameTextfield.getText().equals("") || (passwordTextfield.getText().equals("") && showPasswordTextfield.getText().equals(""));
    }


    private void autofillUser(){
        RegisterController autofill = new RegisterController();

        if (!HttpRequests.check("mauriceheimbach", "/users/findUsername")) {
            autofill.registerUser("Liam Maurice", "Heimbach", "mauriceheimbach", "liam.heimbach@stud.uni-duisburg-essen.de", "maurice", null, true, true);
            autofill.registerUser("Vinusan", "Sivalingam", "vinusansivalingam", "vinusan.sivalingam@stud.uni-duisburg-essen.de", "vinusan", null, true, true);
            autofill.registerUser("Jan", "Sowa", "jansowa", "jan.sowa@stud.uni-duisburg-essen.de", "jan", null, true, true);
            autofill.registerUser("Dennis John", "Peeterman", "dennisjohnpeeterman", "dennis.peeterman@stud.uni-duisburg-essen.de", "dennis", null, true, true);
            autofill.registerUser("Leon", "Pfeil", "leonpfeil", "leon.pfeil@stud.uni-duisburg-essen.de", "leon", null, true, true);
            autofill.registerUser("Sami", "Yildirim", "samiyildirim", "sami.yildirim@stud.uni-duisburg-essen.de", "sami", null, true, true);
        }

        if(!HttpRequests.check("HanSolo1","/users/findUsername")) {
            LocalDate date = null;

            for(int i=0;i<15;i++) {
                date=LocalDate.now().minus(Period.ofDays((new Random().nextInt(365 * 70))));
                List<String> fName = Arrays.asList("Bruce", "Tony", "Luffy", "Dipper", "Jett");
                Collections.shuffle(fName);
                List<String> lName = Arrays.asList("Reiner", "Ash", "Sonic", "Shelby", "Forger");
                Collections.shuffle(lName);
                autofill.registerUser(fName.get(0), lName.get(0),fName.get(0)+"_"+lName.get(0)+i,fName.get(0)+i+"@gmail.com","test", date,false, true);
            }

            autofill.registerUser("Han", "Solo", "HanSolo1", "han@test.de", "test", date, false, true);

        }
    }

}
