package com.sep.server.services;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import com.gargoylesoftware.htmlunit.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sep.server.model.Movie;

import javax.imageio.ImageIO;

@Service
public class ScrapingService {
    WebClient client = new WebClient();
    HtmlPage page;
    HtmlPage moviePage;

    StringBuilder logString = new StringBuilder();
    String[] sorted = {"Adventure", "Action", "Animation" , "Doku" , "Drama" , "Erotic" , "Family" , "Fantasy" , "Horror" , "Comedy" , "Crime" , "Love" , "Music" , "Scifi" , "Other", "Thriller" , "Western"};
    List<HtmlElement> imdbListItems;

    public ResponseEntity<String> startScraping(String URL,int Obergrenze)
    {
        //setup
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        ResponseEntity reEntity;
        int PageCount = (int)Math.ceil((double)Obergrenze/50.0);//50 Filme pro page;
        int moviesLeftToScrape = Obergrenze;

        if(!URL.contains("release_date="))
        {
            reEntity = doScrapeEvenly(URL,Obergrenze);
        }
        else
        {
            //jede iteration entspricht einer page auf imdb
            for(int i=0;i<PageCount;i++)
            {
                //load page
                try
                {
                    page=client.getPage(URL + "&start=" + (1 + 50*i)); //&start=1 für erste page(1 + 50*0), mit jeder iteration werden 50 Filme bearbeitet

                }
                catch(Exception e)
                {
                    System.out.println("ERROR: " + e.getMessage());
                    String response = e.getMessage();
                    return new ResponseEntity(response, HttpStatus.I_AM_A_TEAPOT);
                }
                if(moviesLeftToScrape >= 50)
                {
                    doScrape(50);
                }
                else
                {
                    doScrape(moviesLeftToScrape);
                }

                moviesLeftToScrape -= 50;
            }
            String response ="Finished Scraping";
            reEntity = new ResponseEntity(response, HttpStatus.OK);
        }
        createLog();
        System.out.println("Scrape fertig");
        return reEntity;
    }

    private ResponseEntity<String> doScrapeEvenly(String URL,int Obergrenze)
    {
        //mindestens ~500 filme

        //https://www.delftstack.com/howto/java/current-year-in-java/
        Calendar cal = Calendar.getInstance();
        String scrapeURL;
        int filmeProJahr = (int)Math.ceil((double)Obergrenze/(cal.get(Calendar.YEAR) - 1970.0));
        for(int year = cal.get(Calendar.YEAR);year >= 1970;year--) //get year damit die Funktion auch in der Zukunft funktioniert
        {
            for(int pageNumber = 0; pageNumber <(int)Math.ceil((double)filmeProJahr/50.0); pageNumber++) //innere Schleife damit für jedes Jahr 2 Seiten/100 Filme geladen werden
            {
                scrapeURL = URL + "&release_date=" + year + "-01-01," + year +"-12-31";
                try
                {
                    page =client.getPage(scrapeURL + "&start=" + (1 + 50 * pageNumber));
                }
                catch (Exception e)
                {
                    System.out.println("ERROR: " + e.getMessage());
                    String response = e.getMessage();
                    return new ResponseEntity(response, HttpStatus.I_AM_A_TEAPOT);
                }
                doScrape(50);
            }

        }



        String response ="Finished Scraping";
        return new ResponseEntity(response, HttpStatus.OK);
    }

    private void doScrape(int amount)
    {
        Movie currentMovie = new Movie();
        //div class="lister-item mode advanced"
        imdbListItems = page.getByXPath("//div[@class=\"lister-list\"]/div");
        for(int i = 0;i < amount;i++)  //listItem = höchster node eines einzelnen Filmes
        {
            HtmlElement listItem = imdbListItems.get(i);
            NodeList headerNodeItems = ((Node)listItem.getByXPath("./div[3]/h3").get(0)).getChildNodes();
            String title = headerNodeItems.item(3).getTextContent();
            int release;
            { //gekappselt da releaseString/regex nicht relevant ist
                String releaseString = headerNodeItems.item(5).getTextContent();

                //Manchmal gibt es ausnahmen wie  (2022 TV Movie) oder "(II) (2022)". Daher müssen wir regex nutzen da einfache substrings zu simpel sind
                //https://stackoverflow.com/questions/4662215/how-to-extract-a-substring-using-regex
                Pattern regex = Pattern.compile("[0-9]{4}"); //regexr.com: Bedeutet 4 benachbarte Zahlen zwischen 0-9
                Matcher matcher = regex.matcher(releaseString);

                if(matcher.find())
                {
                    releaseString = matcher.group();
                    release = Integer.parseInt(releaseString);
                }
                else
                {
                    release = 0;
                }
            }


            //Zweite Zeile: Runtime + Genre
            int runtime;
            String genre;

            try
            {
                String runtimeString = ((Node)listItem.getFirstByXPath(".//span[@class=\"runtime\"]")).getTextContent();
                runtime = Integer.parseInt(runtimeString.substring(0,runtimeString.length() - 4)); //-4 Da "(space)min" 4 zeichen sind
            }
            catch (Exception e)
            {
                runtime = 0;
            }

            try
            {
                genre = ((Node)listItem.getFirstByXPath(".//span[@class=\"genre\"]")).getTextContent();
                genre = genre.substring(1).trim();//remove \n

                //sort genre
                String[] genreListToSort = genre.replaceAll(" ","").split(",");
                Map<String,Integer> sortOrder = new HashMap<>();
                for(int j=0; j < sorted.length; j++)
                {
                    sortOrder.put(sorted[j], j);
                }
     //           System.out.println(Arrays.stream(genreListToSort).toList());
                Arrays.sort(genreListToSort,(a,b) -> sortOrder.get(a) - sortOrder.get(b));
     //           System.out.println(Arrays.stream(genreListToSort).toList());
                genre = "";
                for(int j = 0; j < genreListToSort.length;j++)
                {
                    genre += genreListToSort[j] + ", ";
                }
                genre = genre.substring(0,genre.length() - 2);
            }
            catch (Exception e)
            {
                genre = "";
            }



            //Cast: Director + Stars
            List castList = listItem.getByXPath("./div[3]/p[3]/a"); // Liste besteht aus Object. Daher muss es erst zu Node gecastet werden bevor w3c methoden nutzbar sind
            String director;
            //Da es eine variable Anzahl an Stars pro Film geben kann muss durch alle a tags iteriert werden
            //i=1 da Element 0 der Director ist und somit nicht gebraucht wird
            StringBuilder cast = new StringBuilder();

            for(int j = 1; j < castList.size();j++)
            {
                cast.append(((Node) castList.get(j)).getTextContent()).append(",");
            }
            //remove trailing ,
            try
            {
                director = ((Node)castList.get(0)).getTextContent();
                cast = new StringBuilder(cast.substring(0, cast.length() - 1)); //wurde automatisch zu stringbuilder gechanged
            }
            catch (Exception e)
            {
                System.out.println("Error: Kein Cast");
                director = "";
            }


            //um an das Image sowie den Author zu kommen muss man auf eine seperate Seite gehen
            String movieURL;
            movieURL = ((Node)listItem.getFirstByXPath("./div[3]/h3[1]/a")).getAttributes().item(0).getTextContent();
            try
            {
                currentMovie = getImageAndAuthor("https://www.imdb.com" + movieURL,currentMovie,title);
            }
            catch (Exception e)
            {
                System.out.println("Error:" + e.getMessage());
            }

            //create Movie object
            currentMovie.setDirector(director);
            currentMovie.setCast(cast.toString());
            currentMovie.setReleaseYear(release);
            currentMovie.setMovieName(title);
            if(runtime != 0)
            {
                currentMovie.setLength(runtime);
            }
            if(!genre.equals(""))
            {
                currentMovie.setCategory(genre);
            }

            //add movie through movieRestController.add
            ResponseEntity re = MovieService.createMovie(currentMovie);

            //add to log
            if(re.getStatusCode() == HttpStatus.OK)
            {
                //add to log
                logString.append(LocalDateTime.now() + " " + title + " wurde der Datenbank hinzugefügt" + "\n");
            }
            else
            {
                logString.append(LocalDateTime.now() + " " + title + " bereits in der Datenbank" + "\n");
            }



            System.out.println(title);
        }
        System.out.println("Completed Page " + page.toString());
    }

    Movie getImageAndAuthor(String movieURL,Movie currentmovie,String title) throws MalformedURLException, IOException
    {
            moviePage = client.getPage(movieURL);
            String author = "";
            List authorList = moviePage.getByXPath("/html/body/div[2]/main/div/section[1]/section/div[3]/section/section/div[3]/div[2]/div[1]/div[3]/ul/li[2]/div/ul/li");

            for(Object authorNode : authorList)
            {
                author += ((Node)authorNode).getTextContent() + ",";
            }
            //zusatzinformationen per regex entfernen
            author = author.replaceAll("\\(.+?\\)",""); //alles in klammern
            //entferne letztes ,
            author = author.substring(0,author.length()-1);

            BufferedImage banner;
            String bannerURLString;
            URL bannerURL;
            bannerURLString = ((Node)moviePage.getFirstByXPath("//img[@class=\"ipc-image\"]")).getAttributes().getNamedItem("src").getTextContent();
            bannerURL = new URL(bannerURLString);

            banner = ImageIO.read(bannerURL);
            String bannerPath = createImage(title,banner);

            currentmovie.setBannerPath(bannerPath);
            currentmovie.setAuthor(author);
            return currentmovie;

    }

    void createLog()
    {
        try
        {
            Path path = Paths.get("./logs");
            Files.createDirectories(path);
            File file = new File(path + "/" + LocalDateTime.now().toString().replaceAll("[\\\\\\/:\\*?\"<>\\|]","") + ".txt");
            FileUtils.writeStringToFile(file,logString.toString(), Charset.defaultCharset());
        }
        catch (Exception e)
        {
            System.out.println("Couldn't write log file");
        }

    }

    String createImage(String title,BufferedImage image)
    {
        title = title.replaceAll("[\\\\\\/:\\*?\"<>\\|]","");
        try
        {
            Path path = Paths.get("banner");
            Files.createDirectories(path);
            File file = new File(path.toString() + "/" + title + ".jpg");

            ImageIO.write(image,"jpg",file);
            return file.getAbsolutePath();
        }
        catch (Exception e)
        {
            System.out.println("Error: Couldnt write image");
            return "";
        }

    }


}
