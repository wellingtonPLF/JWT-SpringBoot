package project.br.useAuthentication;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.Cookie;

import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.TokenRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UseAuthenticationApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private TokenRepository tokenRepository;
	
	@Test
	void contextLoads() {
				
		//act
		AuthJPA auth = new AuthJPA("Wellington", "well@gmail.com", "12345678");
		HttpEntity<AuthJPA> requestEntity = new HttpEntity<AuthJPA>(auth);
		
		ResponseEntity<StatusResult<String>> authenticate = restTemplate.exchange(
			 	"/usuarios/authentication",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<StatusResult<String>>() {}
        );
		
		Long authID = Long.valueOf(1);
		TokenJPA tokenDB = this.tokenRepository.findByAuthID(authID).orElseThrow();
		
		HttpHeaders headers = new HttpHeaders();		
		headers.add("Cookie", "accessToken=" + tokenDB.getToken());

		ResponseEntity<Boolean> isLoggedIn = restTemplate.exchange("/usuarios/isLoggedIn",
				HttpMethod.GET,
				new HttpEntity<String>(headers),
				Boolean.class);
		
		//assert
		Assertions.assertThat(isLoggedIn.getStatusCode()).isEqualTo(HttpStatus.OK);
		if (authenticate.getBody().getData() != null) {
			Assertions.assertThat(isLoggedIn.getBody().booleanValue()).isEqualTo(true);
		}
		else {
			Assertions.assertThat(isLoggedIn.getBody().booleanValue()).isEqualTo(false);
		}
	}

}
