package project.br.useAuthentication.dtoModel;

import project.br.useAuthentication.enumState.TokenType;
import project.br.useAuthentication.jpaModel.TokenJPA;

public class TokenDTO {
	
	public Long id;
	public String token;
	public UserDTO user;
	public boolean revoked;
	public boolean expired;
	public TokenType tokenType = TokenType.BEARER;

	public TokenDTO() {}
	
	public TokenDTO(TokenJPA token) {
		this.id = token.getId();
		this.user = new UserDTO(token.getUser());
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.expired = token.isExpired();
		this.revoked = token.isRevoked();
		
	}
	
	public TokenDTO(UserDTO user, String token, TokenType type, Boolean expired, Boolean revoked) {
		this.user = user;
		this.token = token;
		this.tokenType = type;
		this.expired = expired;
		this.revoked = revoked;
	}
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
}
