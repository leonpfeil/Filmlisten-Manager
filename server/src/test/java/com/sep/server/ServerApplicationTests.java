/*package com.sep.server;

import com.sep.server.api.UserRestController;
import com.sep.server.dbaccess.RateMovieRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.RateMovie;
import com.sep.server.model.User;
import com.sep.server.services.UserService;
import org.aspectj.lang.annotation.Before;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServerApplicationTests {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RateMovieRepository rateMovieRepository;

	@Test
	public void testCreateUser(){
		User user=new User();
		user.setEmail("MaxMustermann@outlook.de");
		user.setFirstname("Max");
		user.setLastname("Mustermann");
		user.setUsername("Max2000");
		user.setPassword("password");
		user.setDateOfBirth(null);
		user.setAdmin(true);
		user.setPfpImagePath(null);
		user.setTwoFA(false);
		userRepository.save(user);

		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://localhost:8080"+"/users/findUsername"+"?search="+user.getUsername()))
					.build();

			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertEquals("Abfrage ob der Nutzer existiert",true,response.body().toString().equals("true"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testReview() {
		RateMovie rateMovie = new RateMovie();

		rateMovie.setRatingname("mauriceheimbachAquaman");
		rateMovie.setUsername("mauriceheimbach");
		rateMovie.setMovieName("Aquaman");
		rateMovie.setRating(3);
		rateMovie.setRatingCaption("test");
		rateMovie.setRatingText("test");

		rateMovieRepository.save(rateMovie);

		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://localhost:8080" + "/ratemovie/findrating" + "?search=" + rateMovie.getRatingname()))
					.build();

			HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
			assertEquals("Abfrage ob der Nutzer existiert", "mauriceheimbach", response.body().toString().substring(2, 17));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}*/
