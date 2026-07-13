package com.sep.client.controller;

import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import com.sep.client.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//JavaFX Controller Klasse für das Filme anlegen/verändern Fenster
public class ChangeDatabaseController {

    private File banner;
    Path globalPath = Paths.get("banner");
    private Image image;


    @FXML
    private Button doneButton, uploadBannerButton, changeMovieButton, changeDoneButton, changeBannerButton, cancelChangeMovieButton, backToMainPageButton;

    @FXML
    private MenuButton genreMenubutton;

    @FXML
    private TextField yearTextfield, filmnameTextfield, filmlengthTextfield, regisseurTextfield, directorTextfield;

    @FXML
    private TextArea castTextarea;

    @FXML
    private CheckBox adventureCB, actionCB, animationCB, dokuCB, dramaCB, eroticCB, familyCB, fantasyCB, horrorCB, comedyCB, crimeCB, loveCB,
    musicCB, scifiCB, otherCB, thrillerCB, westernCB;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<String> vorschlaegeView;

    ArrayList<CheckBox> checkboxes = new ArrayList<>();
    List<String> filmliste = new ArrayList<>();

    //Methode wird aufgerufen wenn die changeDatabaseView gestartet wird
    public void initialize() {
        updateVorschlaegeView(); // updatet die Vorschlägetabelle, sodass zu Beginn alle Filme angezeigt werden

        //Checkboxen werden in eine Liste gepackt
        checkboxes.add(adventureCB);
        checkboxes.add(actionCB);
        checkboxes.add(animationCB);
        checkboxes.add(dokuCB);
        checkboxes.add(dramaCB);
        checkboxes.add(eroticCB);
        checkboxes.add(familyCB);
        checkboxes.add(fantasyCB);
        checkboxes.add(horrorCB);
        checkboxes.add(comedyCB);
        checkboxes.add(crimeCB);
        checkboxes.add(loveCB);
        checkboxes.add(musicCB);
        checkboxes.add(scifiCB);
        checkboxes.add(otherCB);
        checkboxes.add(thrillerCB);
        checkboxes.add(westernCB);
    }

    //Methode die ausgeführt wird, wenn Knopf "Film hinzufügen" gedrückt wird
    public void onDoneButtonEvent() {

        try {
            //Falls noch Einträge leer sind, wird ein Alert ausgegeben
            if(emptyEntries()) {
                Alerts.emptyEntriesAlert();
                return;
            }
            //Falls der Film(name) schon existiert, wird ein Alert ausgegeben. (302 heißt, dass der Film vorhanden ist)
            if (HttpRequests.get(replaceIlleagalCharacters(filmnameTextfield.getText()),"/movie/existsMovie").statusCode()==302) {
                Alerts.movieExistsAlreadyAlert();
                return;
            }

            try {
                Integer.parseInt(filmlengthTextfield.getText());
                Integer.parseInt(yearTextfield.getText());
            }
            catch (NumberFormatException e){
                Alerts.notAnIntegerAlert();
                return;
            }

            //Ansonsten ist der Upload Erfolgreich und die Vorschläge-View wird geupdatet und es wird eine Meldung ausgegeben
            uploadMovie();
            updateVorschlaegeView();
            Alerts.successMovieUploadAlert(doneButton);

            filmnameTextfield.setText("");
            regisseurTextfield.setText("");
            castTextarea.setText("");
            for(CheckBox box : checkboxes) {
                box.setSelected(false);
            }
            directorTextfield.setText("");
            filmlengthTextfield.setText("");
            yearTextfield.setText("");
            imageView.setImage(null);
            updateVorschlaegeView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Methode zum überprüfen ob Einträge für einen Film fehlen.
    private boolean emptyEntries() {
        String filmname = filmnameTextfield.getText();
        String filmlength = filmlengthTextfield.getText();
        String regisseur = regisseurTextfield.getText();
        String director = directorTextfield.getText();
        String cast = castTextarea.getText();
        String releaseYear = yearTextfield.getText();
        if(filmname.equals("") || filmlength.equals("") || regisseur.equals("") || director.equals("") || cast.equals("") || releaseYear.equals("")) {
            return true;
        }

        for(CheckBox checkbox : checkboxes) {
            if(checkbox.isSelected()) {
                break;
            }
            if(checkbox.getText().equals("Western") && !checkbox.isSelected()) {
                return true;
            }
        }
        return false;
    }

    //Methode zum hochladen eines Films
    private void uploadMovie(){

        JSONObject json = createJSON();

        try {
            HttpRequests.post("/movie/add",json);
            if(banner!=null){
                json.put("bannerPath", banner.getPath());
                HttpResponse<String> response=HttpRequests.post("/movie/setBanner",json);
                if(response.statusCode()!=200){
                    Alerts.errorBannerUpload();
                }
                banner=null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //Methode welche bei jeder Änderung des Textfields filmnameTextfield aufgerufen wird, dient zum filtern der Sicht (View) Vorschläge nach spezifischen Filmen
    public void updateVorschlaegeView() {
        if(!vorschlaegeView.isDisable()) {
            String replaced = replaceIlleagalCharacters(filmnameTextfield.getText());
            HttpResponse<String> response= HttpRequests.get(replaced, "/movie/requestSpecificNames");
            if(response.body()!=null) {
                JSONArray filmlistearray = new JSONArray(response.body());//RequestBody ist ein einziger String mit allen DB Einträgen, welche nach Filmname aus dem Textfield gefiltert sind und wird zu einem JSON Array gemacht
                filmliste = filmlistearray.toList().stream().map(Object::toString).toList();//Aus dem JSON Array wird eine Stringliste mit allen Filmen aus der DB gemacht
                ObservableList<String> filme = FXCollections.observableArrayList();//erst mal eine leere ObservableList, wichtig da hiermit die vorschlägeView geändert wird
                filme.addAll(filmliste); //Die ObservableList wird mit den Filmen aus der Stringliste filmliste gefüllt
                vorschlaegeView.setItems(filme); //Die Tabelle mit den Vorschlägen wird angepasst

            }
        }
    }

    //Methode lädt bei Ausführung Bild in die Klasse und zeigt das Bild an
    public void onUploadBannerButtonEvent(){

        Stage uploadStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Filmbanner wählen");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));

        banner = fileChooser.showOpenDialog(uploadStage);
        image = new Image(banner.getPath());
        imageView.setImage(image);
    }


    //Methode die aufgerufen wird, wenn Knopf Film bearbeiten gedrückt wird
    //Film bearbeiten ist nur aktiv wenn ein Film aus der Vorschläge-View ausgewählt wurde
    public void onChangeMovieButtonEvent() {
        //Es kommt eine Meldung, dass man im Bearbeitungsmodus ist und es werden Knöpfe aktiviert und andere deaktiviert
        Alerts.changeMovieModeActivated(vorschlaegeView.getSelectionModel().getSelectedItem());
        uploadBannerButton.setDisable(true);
        uploadBannerButton.setVisible(false);
        doneButton.setDisable(true);
        doneButton.setVisible(false);
        changeBannerButton.setDisable(false);
        changeBannerButton.setVisible(true);
        changeDoneButton.setDisable(false);
        changeDoneButton.setVisible(true);
        cancelChangeMovieButton.setDisable(false);
        cancelChangeMovieButton.setVisible(true);
        changeMovieButton.setDisable(true);
        vorschlaegeView.setDisable(true);

        String replaced = replaceIlleagalCharacters(vorschlaegeView.getSelectionModel().getSelectedItem());
        JSONArray filmarray = new JSONArray(HttpRequests.get(replaced, "/movie/requestSpecificMovie").body());
        List<String> film = filmarray.toList().stream().map(Object::toString).toList();

        //Textfelder und Checkboxen werden ausgefüllt mit den Einträgen vom Film welchen man bearbeiten möchte
        filmnameTextfield.setText(film.get(0));
        regisseurTextfield.setText(film.get(1));
        castTextarea.setText(film.get(2));
        for(CheckBox box : checkboxes) {
            if(film.get(3).contains(box.getText())) {
                box.setSelected(true);
            }
            else {
                box.setSelected(false);
            }
        }
        directorTextfield.setText(film.get(4));
        filmlengthTextfield.setText(film.get(5));
        yearTextfield.setText(film.get(6));
        if (!(film.get(7).equals(""))) {
            image = new Image(film.get(7));
            imageView.setImage(image);
        }
    }

    //Wird aufgerufen, wenn ein Film aus Vorschläge-View ausgewählt wird und aktiviert den "Film bearbeiten" Knopf
    public void activateChangeButtonEvent() {
        if(vorschlaegeView.getSelectionModel().getSelectedItem() != null) {
            changeMovieButton.setDisable(false);
        }
        else {
            changeMovieButton.setDisable(true);
        }
    }

    //Methode die ausgeführt wird, wenn Knopf "Filmbearbeitung abbrechen" gedrückt wird
    public void onCancelChangeMovieButtonEvent() {
        //Es kommt eine Meldung dass man wieder im Hinzufügemodus ist und es werden Knöpfe aktivert und andere deaktiviert
        //Die Textfelder und Checkboxen werden alle geleert
        Alerts.addMovieModeActivated();
        uploadBannerButton.setDisable(false);
        uploadBannerButton.setVisible(true);
        doneButton.setDisable(false);
        doneButton.setVisible(true);
        changeBannerButton.setDisable(true);
        changeBannerButton.setVisible(false);
        changeDoneButton.setDisable(true);
        changeDoneButton.setVisible(false);
        cancelChangeMovieButton.setDisable(true);
        cancelChangeMovieButton.setVisible(false);
        changeMovieButton.setDisable(false);
        vorschlaegeView.setDisable(false);
        filmnameTextfield.setText("");
        regisseurTextfield.setText("");
        castTextarea.setText("");
        for(CheckBox box : checkboxes) {
            box.setSelected(false);
        }
        directorTextfield.setText("");
        filmlengthTextfield.setText("");
        yearTextfield.setText("");
        banner=null;
        image=null;
        imageView.setImage(null);
        updateVorschlaegeView();
    }

    private JSONObject createJSON(){
        String category="";

        for(CheckBox checkbox : checkboxes) { // Alle Kategorien in einen String
            if (checkbox.isSelected()) {
                category = category + checkbox.getText() + ", ";
            }
        }

        JSONObject json = new JSONObject();
        json.put("movieName",filmnameTextfield.getText());
        json.put("category",category);
        json.put("length",filmlengthTextfield.getText());
        json.put("releaseYear",yearTextfield.getText());
        json.put("author",regisseurTextfield.getText());
        json.put("director",directorTextfield.getText());
        json.put("cast",castTextarea.getText());
        json.put("bannerPath", "");

        return json;

    }

    //Methode die aufgerufen wird, wenn Knopf "Einträge übernehmen" gedrückt wird
    public void onChangeDoneButtonEvent() {

        String alterFilmname = vorschlaegeView.getSelectionModel().getSelectedItem();

        JSONObject alterFilm= new JSONObject();
        alterFilm.put("movieName",alterFilmname);

        String replacedNew = replaceIlleagalCharacters(filmnameTextfield.getText());

        //checkt ob Felder leer sind -> Fehlermeldung und Abbruch
        if(emptyEntries()) {
            Alerts.emptyEntriesAlert();
            return;
        }

        try {
            Integer.parseInt(filmlengthTextfield.getText());
            Integer.parseInt(yearTextfield.getText());
        }
        catch (NumberFormatException e){
            Alerts.notAnIntegerAlert();
            return;
        }

        //Neuer Film(name) darf nicht existieren und alter Film muss noch existieren. (302 heißt, dass ein Film existiert)
        if (!filmnameTextfield.getText().equals(alterFilmname) && HttpRequests.get(replacedNew, "/movie/existsMovie").statusCode()==302) {
            Alerts.movieExistsAlreadyAlert();
            return;
        }

        try {
            HttpRequests.post("/movie/delete", alterFilm);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        uploadMovie();
        Alerts.successMovieChangeAlert(alterFilmname);
        onCancelChangeMovieButtonEvent();
    }

    private String replaceIlleagalCharacters(String string){
        return string.replaceAll(" ", "%20")
                .replaceAll("!","%21")
                .replaceAll("\"","%22")
                .replaceAll("#","%23")
                .replaceAll("\\$","%24")
                .replaceAll("&","%26")
                .replaceAll("'","%27%27")
                .replaceAll("\\(","%28")
                .replaceAll("\\)","%29")
                .replaceAll("\\*","%2A")
                .replaceAll("\\+","%2B")
                .replaceAll(",","%2C")
                .replaceAll("-","%2D")
                .replaceAll("/","%2F")
                .replaceAll(":","%3A")
                .replaceAll(";","%3B")
                .replaceAll("<","%3C")
                .replaceAll("=","%3D")
                .replaceAll(">","%3F")
                .replaceAll("\\?","%40")
                .replaceAll("@","%5B")
                .replaceAll("\\[","%5C")
                //.replaceAll("\","%5D") ?
                .replaceAll("]","%7B")
                .replaceAll("\\{","%7C")
                .replaceAll("}","%7D");
    }

    public void onBackToMainPageButtonEvent(ActionEvent actionEvent) {
        Stage stage = (Stage) backToMainPageButton.getScene().getWindow();
        stage.close();
    }

    //Öffnen aus ReportView
    public void changeMovie(String movieName){
        vorschlaegeView.getSelectionModel().select(movieName);
        onChangeMovieButtonEvent();
    }
}
