package project.br.useAuthentication.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.exception.ExpiredJwtExceptionResult;

@Service
public class JwtService {
	
	@Value("${security.jwt.secretKey}")
	private String SECRET_KEY;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private Date extractExpiration(String token) {
	    return extractClaim(token, Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	    final Claims claims = extractAllClaims(token);
	    return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		try {
		return Jwts
		        .parserBuilder()
		        .setSigningKey(getSignInKey())
		        .build()
		        .parseClaimsJws(token)
		        .getBody();
		}
		catch(Exception e) {
			throw new ExpiredJwtExceptionResult("Expired Token");
		}
	}

	private Key getSignInKey() {
	    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
	    return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(UserDTO userDetails) {
		Map<String, Object> extraClaims = new HashMap<>();
	    return Jwts
	        .builder()
	        .setClaims(extraClaims)
	        .setSubject(userDetails.getUsername())
	        .setIssuedAt(new Date(System.currentTimeMillis()))
	        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600 * 24)) //24h
	        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	        .compact();
	  }
	
	public boolean isTokenValid(String token, UserDTO userDetails) {
	    final String username = extractUsername(token);
	    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
	    return extractExpiration(token).before(new Date());
	}
}
