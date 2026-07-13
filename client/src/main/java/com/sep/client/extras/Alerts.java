package com.sep.client.extras;

import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;

//Klasse für die Erstellung von Fehlermeldungen für Login und Register
public class Alerts {
    //Register Alerts
    public static void successAlert(Button button) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Registrierung erfolgreich!");
        successAlert.setContentText("Sie haben erfolgreich ein neues Projektname-Konto erstellt!");
        successAlert.showAndWait();

        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    public static void successMovieUploadAlert(Button button) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Upload Erfolgreich!");
        successAlert.setContentText("Sie haben erfolgreich einen neuen Film hochgeladen!");
        successAlert.showAndWait();
    }

    public static void successMovieChangeAlert(String moviename) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Änderung Erfolgreich!");
        successAlert.setContentText("Sie haben erfolgreich Änderungen an dem Film "+moviename+" (alter Name) vorgenommen!");
        successAlert.showAndWait();
    }

    public static void changeMovieModeActivated(String moviename) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Sie befinden sich nun im Bearbeitungsmodus!");
        successAlert.setContentText("Sie bearbeiten den Film "+moviename+"!");
        successAlert.showAndWait();
    }

    public static void addMovieModeActivated() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Sie befinden sich nun im Hinzufügemodus!");
        successAlert.setContentText("Bitte tragen Sie Daten für einen neuen Film ein!");
        successAlert.showAndWait();
    }

    public static void emptyFieldsAlert() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setHeaderText("Ein oder mehrere Felder sind leer!");
        emptyFieldAlert.setContentText("Bitte alle Felder ausfüllen!");
        emptyFieldAlert.showAndWait();
    }

    //Watchlist Alerts
    public static void alreadyInWatchlistAlert() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setHeaderText("Dieser Film ist bereits in ihrer Watchlist!");
        emptyFieldAlert.setContentText("Sie können den Film als gesehen markieren um ihn aus der Watchlist zu entfernen.");
        emptyFieldAlert.showAndWait();
    }

    public static void addedToWatchlistAlert() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.INFORMATION);
        emptyFieldAlert.setHeaderText("Sie haben diesen Film zu ihrer Watchlist hinzugefügt!");
        emptyFieldAlert.setContentText("Sie können den Film als gesehen markieren um ihn aus der Watchlist zu entfernen.");
        emptyFieldAlert.showAndWait();
    }

    //Filtereinstellungen Alerts

    public static void selectedFiltersAlert() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.INFORMATION);
        emptyFieldAlert.setHeaderText("Diese Filtereinstellungen werden nun angewendet");
        emptyFieldAlert.setContentText("Sie können die Filtereinstellungen in diesem Menü jederzeit ändern.");
        emptyFieldAlert.showAndWait();
    }

    public static void emptyEntriesAlert() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setHeaderText("Ein oder mehrere Einträge sind leer!");
        emptyFieldAlert.setContentText("Bitte alle Felder ausfüllen und mindestens ein Genre auswählen!");
        emptyFieldAlert.showAndWait();
    }

    public static void movieExistsAlreadyAlert() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setHeaderText("Dieser Filmname existiert bereits!");
        emptyFieldAlert.setContentText("Bitte einen anderen Namen eintragen oder den Film aus der Tabelle auswählen und bearbeiten!");
        emptyFieldAlert.showAndWait();
    }

    public static void errorBannerUpload() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setHeaderText("Fehler.");
        emptyFieldAlert.setContentText("Es gab ein Problem beim Upload des Banners.");
        emptyFieldAlert.showAndWait();
    }

    public static void errorMessagesOpenAlert() {
        Alert errorMessageAlert = new Alert(Alert.AlertType.ERROR);
        errorMessageAlert.setHeaderText("Fehler beim Registrieren!");
        errorMessageAlert.setContentText("Bitte überprüfen Sie Ihre Eingaben!");
        errorMessageAlert.showAndWait();
    }

    //Login Alerts
    public static void giveSQLAlert() {
        Alert sqlconnectionError = new Alert(Alert.AlertType.ERROR);
        sqlconnectionError.setHeaderText("Fehler beim Verbinden mit Datenbank!");
        sqlconnectionError.setContentText("Überprüfen Sie die Verbindung zur Datenbank auf Fehler!");
        sqlconnectionError.showAndWait();
    }

    public static void giveEmptyTextfieldAlert(TextField textField, PasswordField passwordField, TextField showPasswordField) {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setHeaderText("Ein oder mehrere Felder sind leer!");
        emptyFieldAlert.setContentText("Bitte alle Felder ausfüllen!");
        emptyFieldAlert.showAndWait();

        textField.clear();
        passwordField.clear();
        showPasswordField.clear();
    }

    public static void giveWrongUsernameAlert(TextField textField) {
        Alert wrongUsernameAlert = new Alert(Alert.AlertType.ERROR);
        wrongUsernameAlert.setHeaderText("Falscher Benutzername!");
        wrongUsernameAlert.setContentText("Benutzername nicht gefunden, bitte erneut eintragen!");
        wrongUsernameAlert.showAndWait();

        textField.clear();
    }

    public static void giveWrongPasswordAlert(TextField textField) {
        Alert wrongPasswordAlert = new Alert(Alert.AlertType.ERROR);
        wrongPasswordAlert.setHeaderText("Falsches Passwort!");
        wrongPasswordAlert.setContentText("Passwort bitte erneut eintragen!");
        wrongPasswordAlert.showAndWait();

        textField.clear();
    }

    //Authentification Alerts
    public static void giveWrongCodeAlert(TextField textField) {
        Alert wrongCodeAlert = new Alert(Alert.AlertType.ERROR);
        wrongCodeAlert.setHeaderText("Falscher Code!");
        wrongCodeAlert.setContentText("Der Code wurde falsch eingegeben, bitte erneut versuchen!");
        wrongCodeAlert.showAndWait();

        textField.clear();
    }

    public static void giveEmailSuccessAlert() {
        Alert emailSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
        emailSuccessAlert.setHeaderText("E-Mail mit Authentifizierungscode erfolgreich versendet!");
        emailSuccessAlert.setContentText("Falls die E-Mail nicht im normalen Posteingang zu finden ist, überprüfen Sie bitte Ihren Spam Ordner!");
        emailSuccessAlert.showAndWait();
    }

    public static void notAnIntegerAlert() {
        Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
        emptyFieldAlert.setHeaderText("Keine Zahl.");
        emptyFieldAlert.setContentText("Bitte schreiben Sie nur ganze Zahlen in die Felder \"Filmlänge\" und \"Erscheinungsjahr\".");
        emptyFieldAlert.showAndWait();
    }

    public static void twoFaEnabled() {
        Alert twoFaEnabled = new Alert(Alert.AlertType.INFORMATION);
        twoFaEnabled.setHeaderText("Zwei-Faktor-Authentifizierung erfolgreich aktiviert!");
        twoFaEnabled.setContentText("Zwei-Faktor-Authentifizierung erfolgreich für Ihren Account aktiviert, Ihre Daten sind nun sicherer!");
        twoFaEnabled.showAndWait();
    }

    public static void twoFaDisabled() {
        Alert twoFaDisabled = new Alert(Alert.AlertType.INFORMATION);
        twoFaDisabled.setHeaderText("Zwei-Faktor-Authentifizierung erfolgreich deaktiviert!");
        twoFaDisabled.setContentText("Zwei-Faktor-Authentifizierung wurde erfolgreich für ihren Account deaktiviert, beachten Sie, dass dies eine erhebliche Sicherheitslücke darstellen könnte!");
        twoFaDisabled.showAndWait();
    }

    //Privacy Settings Alerts

    public static void publicAlert() {
        Alert publicAlert = new Alert(Alert.AlertType.INFORMATION);
        publicAlert.setHeaderText("Sie haben ihre Privatsphäreeinstellungen erfolgreich angepasst!");
        publicAlert.setContentText("Die ausgewählte List wird nun öffentlich angezeigt.");
        publicAlert.showAndWait();
    }

    public static void onlyFansAlert() {
        Alert onlyFansAlert = new Alert(Alert.AlertType.INFORMATION);
        onlyFansAlert.setHeaderText("Sie haben ihre Privatsphäreeinstellungen erfolgreich angepasst!");
        onlyFansAlert.setContentText("Die ausgewählte Liste wird nun nur ihren Freunden angezeigt!");
        onlyFansAlert.showAndWait();
    }

    public static void privateAlert() {
        Alert privateAlert = new Alert(Alert.AlertType.INFORMATION);
        privateAlert.setHeaderText("Sie haben ihre Privatsphäreeinstellungen erfolgreich angepasst!");
        privateAlert.setContentText("Die ausgewählte Liste wird nun niemanden außer Ihnen angezeigt!");
        privateAlert.showAndWait();
    }

    //Report Alerts

    public static void reportFailedAlert() {
        Alert privateAlert = new Alert(Alert.AlertType.ERROR);
        privateAlert.setHeaderText("Error");
        privateAlert.setContentText("Es gab einen Fehler bei der Übermittlung des Reports.");
        privateAlert.showAndWait();
    }

    public static void reportSentAlert() {
        Alert privateAlert = new Alert(Alert.AlertType.INFORMATION);
        privateAlert.setHeaderText("Danke!");
        privateAlert.setContentText("Der Report wurde gesendet, vielen Dank für Ihre Information.");
        privateAlert.showAndWait();
    }

    //WatchedList Alerts
    public static void movieAlreadyInWatchedList() {
        Alert movieAlreadyInWatchedListAlert = new Alert(Alert.AlertType.ERROR);
        movieAlreadyInWatchedListAlert.setHeaderText("Fehler!");
        movieAlreadyInWatchedListAlert.setContentText("Film ist bereits in der Watchedlist!");
        movieAlreadyInWatchedListAlert.showAndWait();
    }

    public static void movieAddedToWatchedList() {
        Alert movieAddedToWatchedListAlert = new Alert(Alert.AlertType.INFORMATION);
        movieAddedToWatchedListAlert.setHeaderText("Film erfolgreich hinzugefügt!");
        movieAddedToWatchedListAlert.setContentText("Dieser Film wurde erfolgreich zur Watchedlist hinzugefügt!");
        movieAddedToWatchedListAlert.showAndWait();
    }
    public static void ratingSuccessfull() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Rezension hinzugefügt!");
        successAlert.setContentText("Sie haben erfolgreich eine neue Rezension hochgeladen!");
        successAlert.showAndWait();
    }

    public static void ratingDeleted() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Rezension gelöscht!");
        successAlert.setContentText("Sie haben erfolgreich Ihre Bewertung entfernt!");
        successAlert.showAndWait();
    }

    public static void seenMovieDeleted() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Gesehenen Film entfernt");
        successAlert.setContentText("Der Film wurde erfolgreich von der Watchedlist entfernt");
        successAlert.showAndWait();
    }

    public static void statisticResettet() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Statistik gelöscht!");
        successAlert.setContentText("Die Statistik wurde zurückgesetzt!");
        successAlert.showAndWait();
    }

    public static void statisticDownloaded() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setHeaderText("Statistik heruntergeladen!");
        successAlert.setContentText("Die Statistik wurde erfolgreich runtergeladen!");
        successAlert.showAndWait();
    }

    //Movie Invite Alerts

    public static void filmNameEmptyErrorAlert() {
        Alert filmNameEmptyError = new Alert(Alert.AlertType.ERROR);
        filmNameEmptyError.setHeaderText("Filmname ist leer!");
        filmNameEmptyError.setContentText("Bitte einen Filmnamen eingeben!");
        filmNameEmptyError.showAndWait();
    }

    public static void filmNotFoundErrorAlert() {
        Alert filmNotFoundError = new Alert(Alert.AlertType.ERROR);
        filmNotFoundError.setHeaderText("Film wurde nicht gefunden!");
        filmNotFoundError.setContentText("Bitte geben Sie einen in der Datenbank vorhandenen Film ein!");
        filmNotFoundError.showAndWait();
    }

    public static void sendFilmInviteSuccessAlert(String sender, String target, String movieName, String date, String time) {
        Alert sendFilmInviteAlert = new Alert(Alert.AlertType.INFORMATION);
        sendFilmInviteAlert.setHeaderText("Filmeinladung erfolgreich gesendet!");

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                HttpRequests.sendMovieInvitationEmail(sender, getEmailFromUsername(target), movieName, date, time);
                timer.cancel();
            }
        }, 0, 1);


        sendFilmInviteAlert.setContentText("Sobald der Nutzer die Einladung angenommen hat, werden Sie per E-Mail informiert!");
        sendFilmInviteAlert.showAndWait();
    }

    private static String getEmailFromUsername(String username) {
        return HttpRequests.getString(username, "/users/getEmail");
    }

    public static void datePickerEmptyAlert() {
        Alert datePickerEmpty = new Alert(Alert.AlertType.ERROR);
        datePickerEmpty.setHeaderText("Kein Datum gefunden!");
        datePickerEmpty.setContentText("Bitte geben Sie ein Datum ein!");
        datePickerEmpty.showAndWait();
    }

    public static void timeEmptyAlert() {
        Alert dateEmpty = new Alert(Alert.AlertType.ERROR);
        dateEmpty.setHeaderText("Zeitfenster ist leer!");
        dateEmpty.setContentText("Bitte geben Sie eine Uhrzeit für Ihre Filmeinladung an!");
        dateEmpty.showAndWait();
    }

    public static void timeErrorAlert() {
        Alert timeError = new Alert(Alert.AlertType.ERROR);
        timeError.setHeaderText("Ungültige Zeitangabe!");
        timeError.setContentText("Bitte geben Sie eine gültige Zeit für Ihre Filmeinladung an!");
        timeError.showAndWait();
    }

    public static void movieInvitationAccepted() {
        Alert movieInvitationAccepted = new Alert(Alert.AlertType.INFORMATION);
        movieInvitationAccepted.setHeaderText("Filmeinladung erfolgreich angenommen!");
        movieInvitationAccepted.setContentText("Sie können nun den Film auf ihrer Watchliste finden!");
        movieInvitationAccepted.showAndWait();
    }

    public static void movieInvitationDeclined() {
        Alert movieInvitationDeclined = new Alert(Alert.AlertType.INFORMATION);
        movieInvitationDeclined.setHeaderText("Filmeinladung erfolgreich abgelehnt!");
        movieInvitationDeclined.setContentText("");
        movieInvitationDeclined.showAndWait();
    }

    public static void sameUsernameAlert() {
        Alert sameUsernameAlert = new Alert(Alert.AlertType.ERROR);
        sameUsernameAlert.setHeaderText("Eigene Filmeinladungen nicht möglich!");
        sameUsernameAlert.setContentText("Bitte wählen Sie einen anderen Nutzer für Ihre Filmeinladung aus!");
        sameUsernameAlert.showAndWait();
    }

    public static void wrongDateAlert() {
        Alert wrongDateAlert = new Alert(Alert.AlertType.ERROR);
        wrongDateAlert.setHeaderText("Ungültiges Datum ausgewählt!");
        wrongDateAlert.setContentText("Bitte wählen Sie ein gültiges Datum aus!");
        wrongDateAlert.showAndWait();
    }

    //Statistic Alerts
    public static void noDateSelected() {
        Alert noDateSelected = new Alert(Alert.AlertType.ERROR);
        noDateSelected.setHeaderText("Sie haben vergessen einen Zeitraum anzugeben!");
        noDateSelected.setContentText("Bitte wählen Sie ein Start- und ein Enddatum.");
        noDateSelected.showAndWait();
    }

    public static void endDateBeforeStartDate() {
        Alert noDateSelected = new Alert(Alert.AlertType.ERROR);
        noDateSelected.setHeaderText("Das Enddatum befindet sich vor dem Startdatum!");
        noDateSelected.setContentText("Bitte wählen Sie einen gültigen Zeitraum.");
        noDateSelected.showAndWait();
    }

    public static void noDataInThisRange() {
        Alert noData = new Alert(Alert.AlertType.INFORMATION);
        noData.setHeaderText("Für diesen Zeitraum wurden keine Daten gefunden.");
        noData.setContentText("Bitte wählen Sie einen anderen Zeitraum.");
        noData.showAndWait();
    }

    public static void newFavoriteMovie() {
        Alert newFavoriteMovie = new Alert(Alert.AlertType.INFORMATION);
        newFavoriteMovie.setHeaderText("Sie haben diesen Film als Favoriten gespeichert.");
        newFavoriteMovie.setContentText("Sie können jederzeit einen neuen Film als Favoriten speichern");
        newFavoriteMovie.showAndWait();
    }

    public static void noFavoriteMovie() {
        Alert noFav = new Alert(Alert.AlertType.ERROR);
        noFav.setHeaderText("Sie haben keinen Film als Favoriten ausgewählt!");
        noFav.setContentText("Bitte wählen Sie zuerst einen Film als Favoriten aus.");
        noFav.showAndWait();
    }
    public static void genericAlert(String header, String content, Alert.AlertType alertType)
    {
        Alert noData = new Alert(alertType);
        noData.setHeaderText(header);
        noData.setContentText(content);
        noData.showAndWait();
    }
}
