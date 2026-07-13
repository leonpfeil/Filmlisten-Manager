package com.sep.client.controller;

import com.sep.client.Launcher;
import com.sep.client.Main;
import com.sep.client.extras.Alerts;
import com.sep.client.extras.HttpRequests;
import com.sep.client.model.RateMovie;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class UserController {

    @FXML
    private TextField searchBarTextfield;

    @FXML
    private ToggleButton movieToggleButton, userToggleButton, recToggleButton;

    @FXML
    private Button profileButton, reportButton, scrapeButton, logoutButton, changeMovieDatabaseButton, showReportsButton, chatButton, addFriendButton, addToWatchlistButton,
            setFilterButton, filmgesehen, removefromwatchedlist, movieInviteButton, movieInvitationsButton, resetbutton, favoriteButton;

    @FXML
    private ScrollPane watchedScroll, watchScroll, friendScroll;

    @FXML
    private Label usernameLabel, searchedUsernameLabel, filmNameLabel, filmCategorieLabel, filmLengthLabel, filmReleaseYearLabel,
            filmRegisseurLabel, filmDirectorLabel, numberOfReviewsLabel, filmCastLabel, filterSelected, ratinglabel, globalratingLabel, numberwatchedLabel;

    @FXML
    private ToggleGroup searchTypToggleGroup;

    @FXML
    private ListView<String> searchListView;

    @FXML
    private List<String> usernameOfFriends;

    @FXML
    private ImageView usersPFPImageView, searchedUserPFPImageView, selectedMovieBanner;

    @FXML
    private HBox friendsHBox, watchlistHBox, watchedlistHBox,bestMoviesHBox,friendsRecHBox,youRecHBox;

    @FXML
    private Label watchedListLabel, watchListLabel, friendListLabel;

    @FXML
    private Button rateButton, statbutton;

    @FXML
    private AnchorPane movieAnchorPane, userAnchorPane, movieRecAnchorPane;

    private File myPFP;
    private Image image;
    public static String username;
    private String baseImage,selectedUser,selectedUserFriendsList;
    List<String> userList = new ArrayList<>();
    List<String> openChatList  = new ArrayList<>();
    //Stage globalStage;

    List<String> yourMovieRecList  = new ArrayList<>();
    List<String> friendsMovieRecList  = new ArrayList<>();
    List<String> bestMovieList  = new ArrayList<>();
    List<String> filmliste = new ArrayList<>();


    @FXML
    private TableView<RateMovie> reviewtable;

    @FXML
    private TableColumn<RateMovie, String> usernameCol;
    @FXML
    private TableColumn<RateMovie, Void> toProfileButtonCol;

    @FXML
    private TableColumn<RateMovie, String> captionCol;

    @FXML
    private TableColumn<RateMovie, String> textCol;




    private ObservableList<RateMovie> reviewList = FXCollections.observableArrayList();


    public void initialize(){
        //Wenn der User ein Admin ist, werden die Adminbuttons auf visible gesetzt

        //es wird direkt nach den Filemen gesucht, ausser man setzt manuell auf Nutzer
        recToggleButton.setSelected(true);
        recToggleButton.setDisable(true);

       // movieToggleButton.setSelected(true);
       // movieToggleButton.setDisable(true);
        searchTyp();
        try {
            searchForUserAndMovieView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //----Admin Statistik
        statbutton.setVisible(false);
        resetbutton.setVisible(false);
        //----
        movieInviteButton.setVisible(false);
        addFriendButton.setVisible(false);
        chatButton.setVisible(false);

        startChatListener();
        startMovieInvitationListener();
        startFriendListener();
    }
    //linke Seite Benutzeroberfläche
    @FXML
    public void onProfileButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/profileView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        ProfileController profileController = fxmlLoader.getController();
        profileController.setUsername(this.username);

        Stage profileStage = new Stage();
        profileStage.setScene(new Scene(root));
        profileStage.show();
    }

    void startChatListener()
    {
        //Checkt alle 10 sekunden ob jemand mit einem chatten möchte
        Timer chatListener = new Timer();
        chatListener.schedule(new TimerTask() {
            @Override
            public void run(){
                if(username != null)
                {
                    String response = HttpRequests.getByURL("/chat/consumeChatRequest?username=" + username).toString();
                    if(!response.equals(""))
                    {
                        Platform.runLater(() -> createChatDialog(response)); //runlater damit der code auf dem javafx thread ausgeführt wird. sonnst kann man kein dialog öffnen

                    }
                }
            }
        }, 0, 10 * 1000);

        //Registrier ein event um den Timer zu beenden wenn das Fenster geschlossen wird
        //globalStage.setOnCloseRequest(event -> chatListener.cancel());
    }

    void startMovieInvitationListener() {
        Timer movieInvitationListener = new Timer();

        movieInvitationListener.schedule(new TimerTask() {
            @Override
            public void run(){
                if (username != null) {
                    String response = HttpRequests.getByURL("/movieInvitation/consumeMovieInvitationRequest?username=" + username).toString();
                    if(!response.equals("")) {
                        Platform.runLater(() -> {
                            try {
                                createMovieInvitationDialog(response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }); //runlater damit der code auf dem javafx thread ausgeführt wird. sonnst kann man kein dialog öffnen
                    }
                }
            }
        }, 0, 10 * 1000);
    }

    void startFriendListener()
    {

        Timer friendListener = new Timer();

        friendListener.schedule(new TimerTask() {
            @Override
            public void run(){
                if(username != null)
                {
                    String response = HttpRequests.getByURL("/social/consumeFriendRequest?username=" + username).toString();
                    if(!response.equals(""))
                    {
                        Platform.runLater(() -> createFriendDialog(response)); //runlater damit der code auf dem javafx thread ausgeführt wird. sonnst kann man kein dialog öffnen
                    }
                }
            }
        }, 0, 10 * 1000);

        //Registrier ein event um den Timer zu beenden wenn das Fenster geschlossen wird
        //globalStage.setOnCloseRequest(event -> friendListener.cancel());
    }

    //stellt das Design um(Nutzername&Bild)
//stellt das Design um(Nutzername&Bild)
    public void setUsernameForOverlay(String input, boolean isAdmin) {
        this.username = input;
        //TODO Filter einstellen

        createRecommendations();

        if (!HttpRequests.checkIfUserIsAdmin("/users/isAdmin", this.username)) {
            scrapeButton.setVisible(false);
            changeMovieDatabaseButton.setVisible(false);
            showReportsButton.setVisible(false);
            statbutton.setVisible(false);   //Statistik herunterladen
            resetbutton.setVisible(false);  //Statistik einsehen
        } else {
            scrapeButton.setVisible(true);
            changeMovieDatabaseButton.setVisible(true);
            showReportsButton.setVisible(true);
            //rateButton.setVisible(false);
        }

        usernameLabel.setText(username);
        ShowDataController.showBaseImage(username,"/users/pfp", usersPFPImageView);

        //Rating des ersten Films nach dem Login anzeigen
        String moviename = filmNameLabel.getText();
        String username = usernameLabel.getText();
        String ratingname = username + moviename;
        String remove = replaceIlleagalCharacters(ratingname);
        JSONArray array = new JSONArray(HttpRequests.get(remove,"/ratemovie/findrating").body());
        List<String> rating = array.toList().stream().map(Object::toString).toList();
        try {
            ratinglabel.setText(rating.get(2) + " / 5");
        }
        catch (Exception e){

        }
        //------------------------------

    }
    @FXML
    public void onWatchlistButtonAction(ActionEvent event) throws IOException {
        onListButtonAction("/com/sep/client/userProfileListView.fxml","/usersProfile/watchlist");
    }
    @FXML
    public void onWatchedlistButtonAction(ActionEvent event) throws IOException {
        onListButtonAction("/com/sep/client/userProfileListView.fxml","/usersProfile/alreadyWatchedlist");
    }
    @FXML
    public void onFriendsButtonAction(ActionEvent event) throws IOException {
        onListButtonAction("/com/sep/client/userProfileListView.fxml","/usersProfile/friends");
    }

    public void onListButtonAction(String fxmlURL, String httpURL) throws IOException {

        UserProfileListController userProfileListController= new UserProfileListController(this,fxmlURL);
        userProfileListController.setUsername(this.username,httpURL);
        userProfileListController.showStage();
    }

    public void searchForElement(String name, String typ){

        if(typ.equals("Show Movie")){
            movieToggleButton.setSelected(true);
        } else{
            userToggleButton.setSelected(true);
        }
        searchBarTextfield.setText(name);
        try {
            searchForUserAndMovieView();
            showFirstListElement();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //Art von Suche mit ToggleButton
    private void searchTyp(){
        searchTypToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(userToggleButton.isSelected()){

                    userAnchorPane.setDisable(false);
                    userAnchorPane.setVisible(true);
                    movieAnchorPane.setVisible(false);
                    movieAnchorPane.setDisable(true);
                    setFilterButton.setDisable(true);
                    userToggleButton.setDisable(true);
                    movieToggleButton.setDisable(false);
                    movieRecAnchorPane.setDisable(true);
                    movieRecAnchorPane.setVisible(false);
                    recToggleButton.setDisable(false);
                    statbutton.setVisible(false);
                    resetbutton.setVisible(false);
                    try {
                        searchForUserAndMovieView();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    showFirstListElement();
                }else if(movieToggleButton.isSelected()){
                    movieAnchorPane.setDisable(false);
                    userAnchorPane.setDisable(true);
                    movieAnchorPane.setVisible(true);
                    userAnchorPane.setVisible(false);
                    setFilterButton.setDisable(false);
                    userToggleButton.setDisable(false);
                    movieToggleButton.setDisable(true);
                    movieRecAnchorPane.setDisable(true);
                    movieRecAnchorPane.setVisible(false);
                    recToggleButton.setDisable(false);
                    statbutton.setVisible(true);
                    resetbutton.setVisible(true);
                    try {
                        searchForUserAndMovieView();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    showFirstListElement();
                } else{
                    movieAnchorPane.setDisable(true);
                    userAnchorPane.setDisable(true);
                    movieAnchorPane.setVisible(false);
                    userAnchorPane.setVisible(false);
                    setFilterButton.setDisable(true);
                    movieRecAnchorPane.setDisable(false);
                    movieRecAnchorPane.setVisible(true);
                    userToggleButton.setDisable(false);
                    movieToggleButton.setDisable(false);
                    recToggleButton.setDisable(true);
                    statbutton.setVisible(false);
                    resetbutton.setVisible(false);
                    searchListView.getItems().clear();
                }
            }

        });
    };
    private void showFirstListElement(){
        searchListView.getSelectionModel().select(1);
        getSelectedItem();
        searchListView.getSelectionModel().select(0);
        getSelectedItem();
        watchedScroll.setFitToHeight(true);
    }

    //Eintrag in der Liste wird direkt rechts angezeigt
    private void getSelectedItem(){
        searchListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue!=null){
                    if(userToggleButton.isSelected()){
                        movieInviteButton.setVisible(true);
                        addFriendButton.setVisible(true);
                        chatButton.setVisible(true);
                        selectedUser = newValue;



                        changeScrollPanes(newValue);

                        searchedUsernameLabel.setText(newValue);
                        ShowDataController.showBaseImage(newValue,"/users/pfp", searchedUserPFPImageView);
                        if(!newValue.equals(oldValue)){
                            watchedlistHBox.getChildren().clear();
                            friendsHBox.getChildren().clear();
                            watchlistHBox.getChildren().clear();
                        }
                        findElements(newValue,"/usersProfile/watchlist");
                        findElements(newValue,"/usersProfile/friends");
                        findElements(newValue,"/usersProfile/alreadyWatchedlist");
                    }
                    if(movieToggleButton.isSelected()) {
                        String replaced = replaceIlleagalCharacters(searchListView.getSelectionModel().getSelectedItem());
                        JSONArray filmarray = new JSONArray(HttpRequests.get(replaced, "/movie/requestSpecificMovie").body());
                        List<String> film = filmarray.toList().stream().map(Object::toString).toList();

                        //Textfelder und Checkboxen werden ausgefüllt mit den Einträgen vom Film welchen man bearbeiten möchte
                        filmNameLabel.setText(film.get(0));
                        filmRegisseurLabel.setText(film.get(1));
                        filmCastLabel.setText(film.get(2));
                        filmCategorieLabel.setText(film.get(3));
                        filmDirectorLabel.setText(film.get(4));
                        filmLengthLabel.setText(film.get(5) + " Minuten");
                        filmReleaseYearLabel.setText(film.get(6));

                        //Rating für angewählten Film anzeigen
                        String moviename = filmNameLabel.getText();
                        String username = usernameLabel.getText();
                        String ratingname = username + moviename;
                        String remove = replaceIlleagalCharacters(ratingname);
                        JSONArray array = new JSONArray(HttpRequests.get(remove,"/ratemovie/findrating").body());
                        List<String> rating = array.toList().stream().map(Object::toString).toList();
                        try {

                            ratinglabel.setText(rating.get(2) + " / 5");
                        }
                        catch (Exception e){
                            ratinglabel.setText("");
                        }
                        //------------------------------
                        //Reviews anzeigen vom ausgewählten Film
                        reviewList.remove(0, reviewList.size());
                        usernameCol.setCellValueFactory(new PropertyValueFactory<RateMovie, String>("username"));
                        captionCol.setCellValueFactory(new PropertyValueFactory<RateMovie, String>("caption"));
                        textCol.setCellValueFactory(new PropertyValueFactory<RateMovie, String>("text"));

                        String replace = replaceIlleagalCharacters(moviename);
                        JSONArray a = new JSONArray(HttpRequests.get(replace,"/ratemovie/reviews").body());
                        List<String> reviews = a.toList().stream().map(Object::toString).toList();
                        toProfileAction();

    //                  for(String Element : reviews){System.out.println(Element);}
                        try {


                            for(int i = 0; i <a.length(); i++){

                                String b = reviews.get(i);
                                i++;
                                String c = reviews.get(i);
                                i++;
                                String d = reviews.get(i);

                                reviewList.add(new RateMovie(b, c, d));
                                reviewtable.setItems(reviewList);


                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            System.out.println("Aufruf der Rezensionen für den Film " + moviename + " ist fehlgeschlagen");
                        }
                        //Anzahl gesehen
                        JSONArray b = new JSONArray(HttpRequests.get(replace,"/ratemovie/movieseen").body());
                        List<String> seen = b.toList().stream().map(Object::toString).toList();
                        toProfileAction();

                        try {
                            numberwatchedLabel.setText(seen.get(0));
                        }
                        catch (Exception e){

                        }
                        //Anzahl Reviews
                        JSONArray c = new JSONArray(HttpRequests.get(replace,"/ratemovie/countreviews").body());
                        List<String> countreviews = c.toList().stream().map(Object::toString).toList();
                        toProfileAction();

                        try {
                            numberOfReviewsLabel.setText(countreviews.get(0));
                        }
                        catch (Exception e){

                        }
                        //Global-------------------------
                        JSONArray d = new JSONArray(HttpRequests.get(replace,"/movie/getglobal").body());
                        List<String> global = d.toList().stream().map(Object::toString).toList();

                        try {

                            globalratingLabel.setText(global.get(0) + " / 5");
                        }
                        catch (Exception e){
                            globalratingLabel.setText("");
                        }
                        //------------------------------
                        if (!(film.get(7).equals(""))) {
                            image = new Image(film.get(7));
                            selectedMovieBanner.setImage(image);
                        }
                    }
                }
            }
        });
    }

    private void toProfileAction(){

        Callback<TableColumn<RateMovie, Void>, TableCell<RateMovie, Void>> cellFactory = new Callback<TableColumn<RateMovie, Void>, TableCell<RateMovie, Void>>() {
            @Override
            public TableCell<RateMovie, Void> call(final TableColumn<RateMovie, Void> param) {
                final TableCell<RateMovie, Void> cell = new TableCell<RateMovie, Void>() {

                    private final Button btn = new Button("Profil");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            String data = getTableView().getItems().get(getIndex()).getUsername();
                            searchForElement(data," ");
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        toProfileButtonCol.setCellFactory(cellFactory);

    }
    //Wird aufgerufen, wenn Knopf "Vorheriger Film" gedrückt wird. Wählt den vorherigen Film in der searchView aus und aktualisiert angezeigte Einträge
    public void onPreviousMovieButtonEvent() {
        int previous = searchListView.getFocusModel().getFocusedIndex() - 1;
        if(previous != -1) {
            this.searchListView.getSelectionModel().select(previous);
            getSelectedItem();
        }
        else {
            this.searchListView.getSelectionModel().select(searchListView.getItems().size()-1);
            getSelectedItem();
        }
    }

    @FXML
    public void onRateMovieButtonEvent (ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/ratingView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();

        String moviename = filmNameLabel.getText();
        String username = usernameLabel.getText();

        String ratingname = username + moviename;

        String replaced = replaceIlleagalCharacters(ratingname);
        JSONArray array = new JSONArray(HttpRequests.get(replaced,"/ratemovie/findrating").body());
        List<String> rating = array.toList().stream().map(Object::toString).toList();


        RateMovieController controller = fxmlLoader.getController();
        controller.setMoviename(moviename);
        controller.setUsername(username);

        try {
            controller.setRatinglabel(rating.get(2) + " / 5");
            controller.setRatingcaption(rating.get(3));
            controller.setRatingtext(rating.get(4));
        }
        catch (Exception e){
        //    System.out.println("Keine Rezension gefunden");
        }
    }


    //Wird aufgerufen, wenn Knopf "Nächster Film" gedrückt wird. Wählt den nächsten Film in der searchView aus und aktualisiert angezeigte Einträge
    public void onNextMovieButtonEvent() {
        int next = searchListView.getFocusModel().getFocusedIndex() + 1;
        if(next != searchListView.getItems().size()) {
            this.searchListView.getSelectionModel().select(next);
            getSelectedItem();
        }
        else {
            this.searchListView.getSelectionModel().select(0);
            getSelectedItem();
        }
    }

    public void onAddToWatchlistButtonEvent() {
        try {
            String replaced = replaceIlleagalCharacters(filmNameLabel.getText());
            HttpResponse<String> response= HttpRequests.getIsMovieInWatchlist("/movie/isMovieInWatchlist", usernameLabel.getText(), replaced);
            if(response.body().equals("true")) {
                Alerts.alreadyInWatchlistAlert();
            }
            else {
                HttpRequests.postUsernameAndMoviename("/movie/addToWatchlist", replaceIlleagalCharacters(filmNameLabel.getText()), usernameLabel.getText());
                Alerts.addedToWatchlistAlert();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void deleteFromWatchlist() {

        //-------- Film gesehen DB
        JSONObject jsonO = new JSONObject();
        String moviename = filmNameLabel.getText();

        jsonO.put("ratingname", username+moviename);
        jsonO.put("username", username);
        jsonO.put("movieName", moviename);
        jsonO.put("movieSeen", true);
        jsonO.put("rating", 0);
        jsonO.put("ratingCaption", "");
        jsonO.put("ratingText", "");

        try {
            HttpRequests.post("/ratemovie/addseenmovie", jsonO);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //--------

        addToWatchedlist();

        try {
            HttpRequests.postUsernameAndMoviename("/movie/deleteFromWatchlist", replaceIlleagalCharacters(filmNameLabel.getText()), usernameLabel.getText());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addToWatchedlist() {
        try {
            String replaced = replaceIlleagalCharacters(filmNameLabel.getText());
            HttpResponse<String> response= HttpRequests.getIsMovieInWatchlist("/movie/isMovieInWatchedlist", usernameLabel.getText(), replaced);
            if(response.body().equals("true")) {
                Alerts.movieAlreadyInWatchedList();
            }
            else {
                HttpRequests.postUsernameAndMoviename("/movie/addToWatchedlist", replaceIlleagalCharacters(filmNameLabel.getText()), usernameLabel.getText());
                String curDate = LocalDate.now().toString();
                String filmlength = filmLengthLabel.getText().replaceAll(" Minuten", "");
                HttpRequests.postStat("/usersProfile/addStat", usernameLabel.getText(), replaceIlleagalCharacters(curDate), replaceIlleagalCharacters(filmCategorieLabel.getText()), replaceIlleagalCharacters(filmNameLabel.getText()), replaceIlleagalCharacters(filmCastLabel.getText()), filmlength);
                Alerts.movieAddedToWatchedList();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void deleteFromWatchedlist() {

        //---------Film gesehen entfernen DB
        JSONObject jsonO = new JSONObject();
        String moviename = filmNameLabel.getText();
        jsonO.put("ratingname", username+moviename);

        try {
            HttpRequests.post("/ratemovie/delete", jsonO);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //---------
        String removed = replaceIlleagalCharacters(moviename);
        JSONArray g = new JSONArray(HttpRequests.get(removed,"/ratemovie/getglobal").body());
        List<String> global = g.toList().stream().map(Object::toString).toList();

        int grating = 0;
        int count = 0;
        float globalrating = 0;


        try {
            for (int i = 0; i < g.length(); i++) {
                int z = Integer.parseInt(global.get(i));
                grating = grating + z;
                count++;
            }

            globalrating = 0;

            if(count != 0) {

                globalrating = (float) grating / count;
                globalrating = (float) (Math.round(globalrating*10)/10.0);
            }

        }

        catch(Exception e){
            e.printStackTrace();
            System.out.println("Berechnung des globalen Rating fehlgeschlagen");
        }




        JSONObject jsonO2 = new JSONObject();
        jsonO2.put("movieName", filmNameLabel.getText());
        jsonO2.put("globalrating", globalrating);



        try {
            HttpRequests.post("/movie/setglobal", jsonO2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //---------

        try {
            HttpRequests.postUsernameAndMoviename("/movie/deleteFromWatchedlist", replaceIlleagalCharacters(filmNameLabel.getText()), usernameLabel.getText());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Alerts.seenMovieDeleted();
    }

    private void changeScrollPanes(String newValue) {
        String friends = HttpRequests.getString(newValue, "/usersProfile/friends");

        if (HttpRequests.getListPrivacyStatus( "/privacy/getFriendListPrivacyStatus", newValue).equals("0")) {
            friendScroll.setVisible(true);
            friendsHBox.setVisible(true);
            friendListLabel.setText("Freundesliste:");
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getFriendListPrivacyStatus", newValue).equals("1")) {
            if (!friends.contains(this.username)) {
                friendScroll.setVisible(false);
                friendsHBox.setVisible(false);
                friendListLabel.setText("Dieser Nutzer hat die Einsicht auf seine Freundesliste nur für Freunde aktiviert.");
            } else {
                friendListLabel.setText("Freundesliste:");
                friendScroll.setVisible(true);
                friendsHBox.setVisible(true);
            }

        } else if (HttpRequests.getListPrivacyStatus("/privacy/getFriendListPrivacyStatus", newValue).equals("2")) {
            friendListLabel.setText("Dieser Nutzer hat die Einsicht auf seine Freundesliste auf privat.");
            friendScroll.setVisible(false);
            friendsHBox.setVisible(false);
        }

        if (HttpRequests.getListPrivacyStatus("/privacy/getWatchedListPrivacyStatus", newValue).equals("0")) {
            watchedListLabel.setText("Watchedlist:");
            watchedScroll.setVisible(true);
            watchedlistHBox.setVisible(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchedListPrivacyStatus", newValue).equals("1")) {
            if (!friends.contains(this.username)) {
                watchedScroll.setVisible(false);
                watchedlistHBox.setVisible(false);
                watchedListLabel.setText("Dieser Nutzer hat die Einsicht auf seine Freundesliste nur für Freunde aktiviert.");
            } else {
                watchedListLabel.setText("Watchedlist:");
                watchedScroll.setVisible(true);
                watchedlistHBox.setVisible(true);
            }
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchedListPrivacyStatus", newValue).equals("2")) {
            watchedListLabel.setText("Dieser Nutzer hat die Einsicht auf seine Watchedlist auf privat.");
            watchedScroll.setVisible(false);
            watchedlistHBox.setVisible(false);
        }

        if (HttpRequests.getListPrivacyStatus("/privacy/getWatchListPrivacyStatus", newValue).equals("0")) {
            watchListLabel.setText("Watchlist:");
            watchScroll.setVisible(true);
            watchlistHBox.setVisible(true);
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchListPrivacyStatus", newValue).equals("1")) {
            if (!friends.contains(this.username)) {
                watchScroll.setVisible(false);
                watchlistHBox.setVisible(false);
                watchListLabel.setText("Dieser Nutzer hat die Einsicht auf seine Watchlist nur für Freunde aktiviert.");
            } else {
                watchListLabel.setText("Watchlist:");
                watchScroll.setVisible(true);
                watchlistHBox.setVisible(true);
            }
        } else if (HttpRequests.getListPrivacyStatus("/privacy/getWatchListPrivacyStatus", newValue).equals("2")) {
            watchListLabel.setText("Dieser Nutzer hat die Einsicht auf seine Watchlist auf privat.");
            watchScroll.setVisible(false);
            watchlistHBox.setVisible(false);
        }
    }

    //rechte Seite Nutzersuche
    //Nutzersuche
    public void searchForUserAndMovieView() throws IOException, InterruptedException {
        if(userToggleButton.isSelected()) {
            HttpResponse<String> response = HttpRequests.get(searchBarTextfield.getText(), "/users/searchForUser");
            if (response.body() != null) {
                JSONArray userArray = new JSONArray(response.body());
                userList = userArray.toList().stream().map(Object::toString).toList();
                ObservableList<String> users = FXCollections.observableArrayList();
                users.addAll(userList);
                users.remove(username);
                searchListView.setItems(users);
            }
        }
        if(movieToggleButton.isSelected()) {
            if(filterSelected.isVisible() == false) {
                String replaced = replaceIlleagalCharacters(searchBarTextfield.getText());
                HttpResponse<String> response = HttpRequests.get(replaced, "/movie/requestSpecificNames");
                List<String> filmliste = new ArrayList<>();
                if (response.body() != null) {
                    JSONArray filmlistearray = new JSONArray(response.body());//RequestBody ist ein einziger String mit allen DB Einträgen, welche nach Filmname aus dem Textfield gefiltert sind und wird zu einem JSON Array gemacht
                    filmliste = filmlistearray.toList().stream().map(Object::toString).toList();//Aus dem JSON Array wird eine Stringliste mit allen Filmen aus der DB gemacht
                    ObservableList<String> filme = FXCollections.observableArrayList();//erst mal eine leere ObservableList, wichtig da hiermit die vorschlägeView geändert wird
                    filme.addAll(filmliste); //Die ObservableList wird mit den Filmen aus der Stringliste filmliste gefüllt
                    searchListView.setItems(filme); //Die Tabelle mit den Vorschlägen wird angepasst
                }
            }
            else {
                HttpResponse<String> response = HttpRequests.get(this.username, "/movie/getFilters");
                List<String> filterliste = new ArrayList<>();
                if (response.body() != null && !response.body().equals("[]")) {
                    JSONArray filterarray = new JSONArray(response.body());//RequestBody ist ein einziger String mit allen DB Einträgen, welche nach Filmname aus dem Textfield gefiltert sind und wird zu einem JSON Array gemacht
                    filterliste = filterarray.toList().stream().map(Object::toString).toList();//Aus dem JSON Array wird eine Stringliste mit allen Filmen aus der DB gemacht
                    String filmlengthFilter = replaceIlleagalCharacters(filterliste.get(0));
                    String releaseYearFilter = replaceIlleagalCharacters(filterliste.get(1));
                    String regisseurFilter = replaceIlleagalCharacters(filterliste.get(2));
                    String directorFilter = replaceIlleagalCharacters(filterliste.get(3));
                    String castFilter = replaceIlleagalCharacters(filterliste.get(4));
                    String categoryFilter = replaceIlleagalCharacters(filterliste.get(5));

                    String replaced = replaceIlleagalCharacters(searchBarTextfield.getText());
                    response = HttpRequests.getMoviesWithFilters("/movie/requestSpecificNamesWithFilters", replaced, filmlengthFilter, releaseYearFilter, regisseurFilter, directorFilter, castFilter, categoryFilter);
                    List<String> filmliste = new ArrayList<>();
                    if (response.body() != null) {
                        JSONArray filmlistearray = new JSONArray(response.body());//RequestBody ist ein einziger String mit allen DB Einträgen, welche nach Filmname aus dem Textfield gefiltert sind und wird zu einem JSON Array gemacht
                        filmliste = filmlistearray.toList().stream().map(Object::toString).toList();//Aus dem JSON Array wird eine Stringliste mit allen Filmen aus der DB gemacht
                        ObservableList<String> filme = FXCollections.observableArrayList();//erst mal eine leere ObservableList, wichtig da hiermit die vorschlägeView geändert wird
                        filme.addAll(filmliste); //Die ObservableList wird mit den Filmen aus der Stringliste filmliste gefüllt
                        searchListView.setItems(filme); //Die Tabelle mit den Vorschlägen wird angepasst
                    }
                }
            }
        }
    }

    public void activateFilterWarning() {
        filterSelected.setVisible(true);
    }

    public void deactivateFilterWarning() {
        filterSelected.setVisible(false);
    }

    private void findElements(String username, String urlPath){
        List<String> listOfElementsNames= new ArrayList<>();
        String elementsNames = HttpRequests.getString(username,urlPath);
        listOfElementsNames= Stream.of((elementsNames.split(",#,"))).collect(Collectors.toList());
        fillHBox(urlPath,listOfElementsNames);

    }


    private void fillHBox(String urlPath,List<String> listOfElementsNames){
        switch(urlPath){
            case "/usersProfile/watchlist": showElements(listOfElementsNames,watchlistHBox,"/movie/banner");
                break;
            case "/usersProfile/friends":   showElements(listOfElementsNames,friendsHBox,"/users/pfp");
                break;
            case "/usersProfile/alreadyWatchedlist": showElements(listOfElementsNames,watchedlistHBox,"/movie/banner");
                break;
            case "/usersProfile/getRecommendation": showElements(listOfElementsNames,youRecHBox,"/movie/banner");
                break;
            case "/usersProfile/getFriendsRecommendation": showElements(listOfElementsNames,friendsRecHBox,"/movie/banner");
                break;
            case "/movie/bestMovies":
                showElements(listOfElementsNames,bestMoviesHBox,"/movie/banner");
                break;
            default:
                System.out.println("Fehler");
                break;

        }
    }


    private void showElements(List<String> lists,HBox hBox,String urlPath){
        if(lists.size()!=0) {
            try {
                for (String element : lists) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/sep/client/showDataView.fxml"));
                    VBox vBox = fxmlLoader.load();

                    ShowDataController showDataController = fxmlLoader.getController();
                    showDataController.setUserController(this);
                    showDataController.setData(element, urlPath);

                    hBox.getChildren().add(vBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onReportButtonEvent() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/reportView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        ReportController reportController = fxmlLoader.getController();
        reportController.getMovieLabelAndUsername(searchListView.getSelectionModel().getSelectedItem(), this.username);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        }

    public void onSetFilterButtonEvent() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/mainpageMovieFilterView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        FilterController filterController = fxmlLoader.getController();
        filterController.setUsernameAndFilters(this.username);

        Stage filterStage = new Stage();
        filterStage.setScene(new Scene(root));

        filterStage.showAndWait();

        HttpResponse<String> response= HttpRequests.get(this.username,"/movie/areFiltersEnabled");
        if(response.body().equals("true")) {
            activateFilterWarning();
            try {
                searchForUserAndMovieView();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            deactivateFilterWarning();
            try {
                searchForUserAndMovieView();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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

    @FXML
    public void onScrapeButtonEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/scrapingView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    @FXML
    public void onChangeMovieDatabaseButtonEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/changeDatabaseView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public void onShowReportsButtonEvent(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/openReportsView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public void onUserToggleButtonEvent(ActionEvent event) throws IOException{

    }
    public void onMovieToggleButtonEvent(ActionEvent event) throws IOException{
        movieInviteButton.setVisible(false);
        addFriendButton.setVisible(false);
        chatButton.setVisible(false);
    }

    public void onAddFriendButton(ActionEvent event) throws IOException {
        String elementsNames = HttpRequests.getString(username, "/usersProfile/friends");
        if (!elementsNames.contains(selectedUser) && selectedUser != username) {
            HttpRequests.postStringsByURL("/social/addFriendRequest?requested=" + selectedUser + "&requester=" + username);
        }

    }
    void addFriend()
    {
        HttpRequests.postStringsByURL("/usersProfile/addFriends?username=" + username + "&newFriend=" + selectedUser);
    }

    @FXML
    private void onLogoutButtonEvent(ActionEvent event) throws IOException {
        ((Stage) logoutButton.getScene().getWindow()).close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/loginView.fxml"));
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(fxmlLoader.load()));
        loginStage.show();
    }

    public void onChatButtonEvent(ActionEvent event){
        startChat();
    }
    void startChat()
    {
        try
        {
            //Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"ChatClient-1.jar\" " + username + " " + selectedUser}); //start console client
            java.awt.Desktop.getDesktop().browse(new URI("http://localhost:8080/?username=" + username + "&username2=" + selectedUser + "&group=0")); //start webclient
            if(!openChatList.contains(selectedUser))
            {
                HttpRequests.postStringsByURL("/chat/addRequest?requested=" + selectedUser + "&requester=" + username);
                openChatList.add(selectedUser);
            }


        }
        catch (Exception e)
        {

        }
    }

    Runnable createChatDialog(String response)
    {
        Dialog dialog = new Dialog<ButtonType>();
        ButtonType annehmenButton = new ButtonType("Annehmen", ButtonBar.ButtonData.OK_DONE);
        ButtonType ablehnenButton = new ButtonType("Ablehnen", ButtonBar.ButtonData.NO);
        dialog.setContentText(response + " möchte mit dir Chatten");
        dialog.getDialogPane().getButtonTypes().add(annehmenButton);
        dialog.getDialogPane().getButtonTypes().add(ablehnenButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && (result.get() == annehmenButton))
        {
            selectedUser = response;
            openChatList.add(selectedUser);
            startChat();
        }

        return null;
    }

    Runnable createMovieInvitationDialog(String response) throws IOException {
        Dialog dialog = new Dialog<ButtonType>();
        ButtonType doneButton = new ButtonType("Verstanden", ButtonBar.ButtonData.OK_DONE);
        ButtonType showButton = new ButtonType("Zeige Einladungen", ButtonBar.ButtonData.OK_DONE);
        dialog.setContentText("Du wurdest zu einem Film eingeladen, öffne die Filmeinladungsvorschau für mehr Informationen!");
        dialog.getDialogPane().getButtonTypes().add(doneButton);
        dialog.getDialogPane().getButtonTypes().add(showButton);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && (result.get() == showButton)) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/openMovieInvitationsView.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            Stage openMovieInvitationsStage = new Stage();
            openMovieInvitationsStage.setScene(new Scene(root));
            openMovieInvitationsStage.show();
        }

        return null;
    }

    Runnable createFriendDialog(String response)
    {
        Dialog dialog = new Dialog<ButtonType>();
        ButtonType annehmenButton = new ButtonType("Annehmen", ButtonBar.ButtonData.OK_DONE);
        ButtonType ablehnenButton = new ButtonType("Ablehnen", ButtonBar.ButtonData.NO);
        dialog.setContentText(response + " möchte dir eine Freundschaftsanfrage senden");
        dialog.getDialogPane().getButtonTypes().add(annehmenButton);
        dialog.getDialogPane().getButtonTypes().add(ablehnenButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && (result.get() == annehmenButton))
        {
            selectedUser = response;
            addFriend();
        }

        return null;
    }


    @FXML
    public void onMovieInviteButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/movieInviteView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        MovieInviteController movieInviteController = fxmlLoader.getController();
        movieInviteController.getEnteredUsername(this.username, this.selectedUser);

        Stage movieInviteStage = new Stage();
        movieInviteStage.setScene(new Scene(root));
        movieInviteStage.show();
    }

    @FXML
    public void onMovieInvitationsButtonEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/openMovieInvitationsView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        Stage openMovieInvitationsStage = new Stage();
        openMovieInvitationsStage.setScene(new Scene(root));
        openMovieInvitationsStage.show();
    }

    @FXML
    public void onGroupButtonEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/sep/client/groupChatOverviewView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        Stage groupChatOverview = new Stage();
        groupChatOverview.setScene(new Scene(root));
        groupChatOverview.show();
    }

    @FXML
    public void resetStatistic(){

        float globalrating = 0;
        String moviename = filmNameLabel.getText();

        JSONObject jsonO = new JSONObject();
        jsonO.put("movieName", moviename);
        jsonO.put("globalrating", globalrating);

        try {
            HttpRequests.post("/movie/resetglobal", jsonO);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //----------------


        JSONObject jsonO2 = new JSONObject();
        jsonO2.put("movieName", moviename);


        try {
            HttpRequests.post("/ratemovie/resetstatistic", jsonO2);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Alerts.statisticResettet();
    }

    @FXML
    public void downloadStatistic(){
        String filename = filmNameLabel.getText()+"-Statistik";

        try {
            FileWriter myWriter = new FileWriter(filename+".txt");

            myWriter.write("Filmname: " + filmNameLabel.getText());
            myWriter.write("\n");
            myWriter.write("Globales Bewertung: " + globalratingLabel.getText());
            myWriter.write("\n");
            myWriter.write("Anzahl der Bewertungen: " + numberOfReviewsLabel.getText());
            myWriter.write("\n");
            myWriter.write("Anzahl gesehen: " + numberwatchedLabel.getText());

            myWriter.close();
            System.out.println("Statistik zum Film " + filmNameLabel.getText() + " erfolgreich heruntergeladen.");
        } catch (IOException e) {
            System.out.println("Herunterladen der Statistik fehlgeschlagen!");
            e.printStackTrace();
        }

        Alerts.statisticDownloaded();
    }

    @FXML
    public void onRecToggleButtonEvent(ActionEvent actionEvent) {
       youRecHBox.getChildren().clear();
       bestMoviesHBox.getChildren().clear();
       friendsRecHBox.getChildren().clear();
       createRecommendations();
    }

    private void createRecommendations(){
        bestMovieList = HttpRequests.setJSONList(username, "/movie/bestMovies");
        if(!bestMovieList.isEmpty()) {
            yourMovieRecList = HttpRequests.setJSONList(username, "/usersProfile/getRecommendation");
            fillHBox("/usersProfile/getRecommendation", yourMovieRecList);
            friendsMovieRecList = HttpRequests.setJSONList(username, "/usersProfile/getFriendsRecommendation");
            fillHBox("/usersProfile/getFriendsRecommendation", friendsMovieRecList);
            fillHBox("/movie/bestMovies", bestMovieList);
        }
    }

    public void onFavoriteMovieButtonEvent() {
        try{
            HttpRequests.postUsernameAndMoviename("/usersProfile/setFavoriteMovie",usernameLabel.getText(), filmNameLabel.getText());
            Alerts.newFavoriteMovie();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
