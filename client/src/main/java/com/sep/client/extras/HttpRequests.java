package com.sep.client.extras;

import com.sep.client.controller.AuthentificationController;
import com.sep.client.controller.RateMovieController;
import com.sep.client.model.Report;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.spec.ECField;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Klasse für Http Requests vom Client zum Server
public class HttpRequests {
    static String code;
    //(HTTP-POST) überträgt ein JsonObjekt zum Server um Sachen in eine Datenbank zuschreiben
    public static HttpResponse<String> post(String url, JSONObject jsonObject) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<String> postUsernameAndMoviename(String url,String username, String moviename) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(moviename))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //Sendet Parameter User und Moviename an den Server um einen Boolean zurückzubekommen, ist true wenn Film bereits in der Watchlist ist
    public static HttpResponse<String> getIsMovieInWatchlist(String url,String username, String moviename) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?moviename="+moviename+"&username="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<String> postFilter(String url, String username, String filmlength, String releaseYear, String regisseur, String director, String cast, String category) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username+"&filmlength="+filmlength+"&releaseYear="+releaseYear+"&regisseur="+regisseur
                            +"&director=" +director +"&cast="+cast+"&category="+category))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(filmlength))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<String> postStat(String url, String username, String date, String category, String moviename, String cast, String filmlength) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username+"&moviename="+moviename+"&date="+date+"&cast="+cast+"&category="+category+"&filmlength="+filmlength))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(moviename))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //benutzt um ein PFP auf der Festplatte zuspeichern: Übergibt username um User zusuchen und das Bild zubenennen und den Base64-String
    public static HttpResponse<String> postString(String url,String username, String baseImage) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(baseImage))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //(HTTP-Get) gibt einen String-Wert aus der Datenbank zurück, welcher alle Einträge mit dem Filterkriterium string enthält
    public static HttpResponse<String> get(String string, String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+string))
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<String> getStats(String url, String username, String startDate, String endDate) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username+"&startDate="+startDate+"&endDate="+endDate))
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<String> getMoviesWithFilters(String url,String moviename, String filmlength, String releaseYear, String regisseur, String director, String cast, String category) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?moviename="+moviename+"&filmlength="+filmlength+"&releaseYear="+releaseYear+"&regisseur="+regisseur+
                            "&director="+director+"&cast="+cast+"&category="+category))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .GET()
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //benutzt um PFPs anzuzeigem: übergibt einen Nutzernamen/Bildname und gibt ein Base64 String zurück
    public static String getString(String string, String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+string))
                    .GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().toString();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getStringBool(String string,Boolean bool, String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+string+"&bool="+bool))
                    .GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().toString();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //(HTTP-CHECK) gibt ein Boolean-Wert zurück, ob der String in einer Datenbank enthalten
    public static boolean check(String string, String url) {
        boolean check = false;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?search="+string))
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body().toString().equals("true")) {
                    check = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;
    }

    public static boolean checkPassword(String username, String password, String url) {
        boolean check = false;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?password="+password+"&username="+username))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body().toString().equals("true")) {
                    check = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;
    }

    public static void sendEmail(String url, String receiver) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?receiver="+receiver))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body().toString().equals("Success")) {
                    System.out.println("Email sent successfully!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMovieInvitationEmail(String sender, String target, String movieName, String date, String time) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/email/MovieInvitationGet"+"?target="+target+"&sender="+sender+"&movieName="+movieName+"&date="+date+"&time="+time))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body().toString().equals("Success")) {
                    System.out.println("Email sent successfully!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMovieInvitationAcceptedEmail(String target) {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/email/MovieInvitationAccepted"+"?target="+target))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body().toString().equals("Success")) {
                    System.out.println("Email sent successfully!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfUserIsAdmin(String url, String username) {
        boolean check = false;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body().toString().equals("true")) {
                    check = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;

    }

    public static void dropAuthTable() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/auth/drop"))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body().toString().equals("Success")) {
                System.out.println("Auth table dropped successfully!");
            } else if (response.body().toString().equals("Error")) {
                System.out.println("Drop table error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromMovieInvitation(String sender, String target, String movieName) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/movieInvitation/delete"+"?sender="+sender+"&target="+target+"&movieName="+movieName))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(sender))
                    .POST(HttpRequest.BodyPublishers.ofString(target))
                    .POST(HttpRequest.BodyPublishers.ofString(movieName))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            if (response.body().toString().equals("Success")) {
                System.out.println("MovieInvitation deleted successfully!");
            } else if (response.body().toString().equals("Error")) {
                System.out.println("Deletion error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCodeFromAuth(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/auth/code"+"?username="+username))
                    .GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean checkIfTwoFAEnabled(String username) {
        boolean check = false;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/users/twoFA"+"?username="+username))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                if (response.body().toString().equals("true")) {
                    check = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;
    }


    public static String enableTwoFA(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/users/enableTwoFA"+"?username="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(username))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String disableTwoFA(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/users/disableTwoFA"+"?username="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(username))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getListPrivacyStatus(String url, String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username))
                    .GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createPrivacySettings(String username) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/privacy/create"+"?username="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(username))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPrivacySettings(String username, String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?username="+username))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(username))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject createUserJSONObject(String firstname, String lastname, String username, String email, String password, LocalDate dateOfBirth, Boolean isAdmin, Boolean hasTwoFa) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("firstname", firstname);
        jsonObject.put("lastname", lastname);
        jsonObject.put("username", username);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("dateOfBirth", dateOfBirth);
        jsonObject.put("isAdmin", isAdmin);
        jsonObject.put("hasTwoFA", hasTwoFa);

        return jsonObject;
    }

    public static JSONObject createUserProfileJSONObject(String username,String watchlist, String watchedlist,String friendslist) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("username", username);
        jsonObject.put("watchlist", watchlist);
        jsonObject.put("watchedlist", watchedlist);
        jsonObject.put("friendslist", friendslist);


        return jsonObject;
    }

    public static JSONObject createMovieInviteJSONObject(String movieName, String time, String areaText, String date, String sender, String target, Boolean done) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("moviename", movieName);
        jsonObject.put("time", time);
        jsonObject.put("text", areaText);
        jsonObject.put("date", date);
        jsonObject.put("sender", sender);
        jsonObject.put("target", target);
        jsonObject.put("done", done);

        return jsonObject;
    }

    //Statt dass die URL in der methode mit vorgefertigten parametern zusammengebaut wird, kann man hier die URL selbst bauen.
    public static void postStringsByURL(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(url))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Die response muss in den passenden typ gecastet werden
    public static Object getByURL(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url))
                    .GET()
                    .build();
            HttpResponse response = client.send(request,HttpResponse.BodyHandlers.ofString());
            return response.body();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<List<RateMovieController>> getByURLforInvitations(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url))
                    .GET()
                    .build();
            HttpResponse response = client.send(request,HttpResponse.BodyHandlers.ofString());
            return response;
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HttpResponse<String> postWithReqParam(String url, JSONObject json, String param) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+url+"?param="+param))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



   /* public static HttpResponse<String> getReportList() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/report/getAll"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static HttpResponse<String> changeMovieInvitationToDone(String sender, String target, String movieName) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080"+"/movieInvitation/change"+"?sender="+sender+"&target="+target+"&movieName="+movieName))
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(sender))
                    .POST(HttpRequest.BodyPublishers.ofString(target))
                    .POST(HttpRequest.BodyPublishers.ofString(movieName))
                    .build();

             return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> setJSONList(String username,String url) {
        HttpResponse<String> response = HttpRequests.get(username, url);
        List<String> list = new ArrayList();
        if (response.statusCode()!=500 &&response.body() != null && !response.body().equals("")) {
            JSONArray userArray = new JSONArray(response.body());
            list = userArray.toList().stream().map(Object::toString).toList();
        }
        return list;
    }



        public static String replaceIlleagalCharacters(String string){
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
}
