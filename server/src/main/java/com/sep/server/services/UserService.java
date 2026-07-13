package com.sep.server.services;


import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.Movie;
import com.sep.server.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //.save ist gegeben, speichert Eintrag in die Datenbank
    public ResponseEntity<String> createUser(User user)  {
        userRepository.save(user);
        String response ="created: "+user.toString();
        return new ResponseEntity(response, HttpStatus.OK);
    }
    //gibt ein Boolwert als String zurück, wenn der gesuchte String existiert
    public ResponseEntity<String> findPersonByUsername(String searchVal) {
       String bool;

        if (userRepository.existsByUsername(searchVal)) {
            bool = "true";
        } else {
            bool = "false";
        }

        return new ResponseEntity(bool, HttpStatus.OK);
    }

    public ResponseEntity<String> findPersonByPassword(String searchVal) {
        String bool;

        if (userRepository.existsByPassword(searchVal)) {
            bool = "true";
        } else {
            bool = "false";
        }

        return new ResponseEntity(bool, HttpStatus.OK);
    }

    public ResponseEntity<String> findPersonByEmail(String searchVal) {
        String bool;

        if (userRepository.existsByEmail(searchVal)) {
            bool = "true";
        } else {
            bool = "false";
        }
        return new ResponseEntity(bool, HttpStatus.OK);
    }

    public ResponseEntity<String> findPasswordByUsername(String password, String username) {
        String bool = "false";


            if (userRepository.existsByPasswordAndUsername(password, username)) {
                bool = "true";
            }


        return new ResponseEntity(bool, HttpStatus.OK);
    }


    public ResponseEntity<String> savePFP(String username, String imagePFP) throws IOException {

        User pfpUser= userRepository.getUserByUsername(username);

       //Dateipfad wird erstellt
        Path realPath = Paths.get("allPFPs");
        Files.createDirectories(realPath);
        Path noPfpDatei= Paths.get("./allPFPs/" + "noPFP" + ".jpg");
        File file;
        if(PicToString(noPfpDatei).equals(imagePFP)){
             file = new File(noPfpDatei.toString());

        }else{ file = new File(realPath.toString() + "/" + username+ ".jpg");}


        File firstChange =new File(noPfpDatei.toString());
        if(pfpUser.getPfpImagePath()!=null&&!pfpUser.getPfpImagePath().equals(firstChange.getAbsolutePath())){
            Path path = Paths.get("./allPFPs/" + username + ".jpg");
            Files.delete(path);
        }

        //Bild wird erzeugt und gespeichert
          byte [] image= Base64.getDecoder().decode(imagePFP);
          InputStream is = new ByteArrayInputStream(image);
          BufferedImage pfp = ImageIO.read(is);
          ImageIO.write(pfp,"jpg",file);

        //Dateipfasd vom Profilbild wird in der DB gesapeichert
        pfpUser.setPfpImagePath(file.getAbsolutePath());
        userRepository.save(pfpUser);



        return new ResponseEntity("PFP gespeichert",HttpStatus.OK);
    }

    public ResponseEntity<String> showPFP(String searchVal) {
        User pfpUser = userRepository.getUserByUsername(searchVal);

        File allPFPs;
        Path path;
        String baseImage="";
        //Bild wird gesucht
        if(pfpUser!=null) {
            if (searchVal.equals("noPFP") || pfpUser.getPfpImagePath() == null) {
                path = Paths.get("./allPFPs/" + "noPFP" + ".jpg");
            } else {
                path = Paths.get(pfpUser.getPfpImagePath());
            }} else{
            path = Paths.get("./allPFPs/" + "noPFP" + ".jpg");
        }
            //Bild wird in Base64-String codeirt
            baseImage=PicToString(path);

        return new ResponseEntity(baseImage,HttpStatus.OK);
    }

    private static String PicToString(Path path){
        File allPFPs;
        String picAsString="";
        allPFPs = new File(path.toString());
        byte[] bytes = new byte[(int) allPFPs.length()];
        try {
            FileInputStream fis = new FileInputStream(allPFPs);
            fis.read(bytes);
            fis.close();
            picAsString = Base64.getEncoder().encodeToString(bytes);
            return picAsString;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim PFP anzeigen" + e.getMessage());
        }
        return null;

    }

    public ResponseEntity<String> isAdmin(String username) {
        String bool = "";

        try {
            User user = userRepository.getUserByUsername(username);

            if (!user.getAdmin()) {
                bool = "false";
            } else {
                bool = "true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(bool, HttpStatus.OK);
    }

    public ResponseEntity<String> getEmailFromUsername(String username) {
        try {
            User user = userRepository.getUserByUsername(username);
            String email = user.getEmail();

            return new ResponseEntity(email, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResponseEntity<List<String>> searchForUser(String searchVal) {
        List<String> users= new ArrayList<>();
      //  for(User user : userRepository.findUserByUsernameContaining(searchVal)){
      //      users.add(user.getUsername());
      //  }

        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/database", "root", "")) {
            PreparedStatement state = con.prepareStatement("select * from user where username like '%"+searchVal+"%'");
            ResultSet rs = state.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                users.add(username);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity(users, HttpStatus.OK);
    }

    public ResponseEntity<String> hasTwoFA(String username) {
        String bool = "";

        try {
            User user = userRepository.getUserByUsername(username);

            if (!user.getTwoFA()) {
                bool = "false";
            } else {
                bool = "true";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(bool, HttpStatus.OK);
    }

    public void enableTwoFA(String username) {
        try {
            User user = userRepository.getUserByUsername(username);

            user.setTwoFA(true);
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableTwoFA(String username) {
        try {
            User user = userRepository.getUserByUsername(username);

            user.setTwoFA(false);
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}