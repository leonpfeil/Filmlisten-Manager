package com.sep.client.controller;

import com.sep.client.extras.HttpRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Base64;

public class ShowDataController {

    @FXML
    private ImageView CoverImageView;

    @FXML
    private Label TitleLabel;

    @FXML
    private Button toMovieButton;
    private UserController userController;

    private String buttonText;

    public void setUserController(UserController userController){
        this.userController=userController;
    }
    @FXML
    public void onLinkButtonAction(ActionEvent event){
        userController.searchForElement(TitleLabel.getText(),buttonText);
    }

    public static void showBaseImage(String name, String urlPath, ImageView imageView){
        String imagePFP= HttpRequests.getString(HttpRequests.replaceIlleagalCharacters(name),urlPath);
        byte [] byteImage= Base64.getDecoder().decode(imagePFP);
        InputStream is = new ByteArrayInputStream(byteImage);
        Image image = new Image(is);
        imageView.setImage(image);
    }

    public static String chooseImage(File file,Image image,String string,ImageView imageView){
        Stage uploadStage = new Stage();
        //möglichkeit um das Bild zusuchen
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Profilbild wählen");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));

        file = fileChooser.showOpenDialog(uploadStage);
        //formattiert das Bild in einen Base64 String
        byte [] bytes= new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();
            string= Base64.getEncoder().encodeToString(bytes);
            //zeigt das Bild im Client an
            image = new Image(file.getPath());
            imageView.setImage(image);
            return string;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setData(String name,String urlPath){
        if(!name.equals("")) {
            toMovieButton.setVisible(true);
            TitleLabel.setVisible(true);
            showBaseImage(name, urlPath, CoverImageView);
            if(urlPath.equals("/movie/banner")){
                buttonText="Show Movie";
            }else{
                buttonText="Show Friend";

            }
            TitleLabel.setText(name);

        }
    }
}
