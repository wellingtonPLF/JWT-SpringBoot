package project.br.useAuthentication.jpaModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import project.br.useAuthentication.dtoModel.TokenDTO;
import project.br.useAuthentication.enumState.TokenType;

@Entity
@Table(name = "token")
public class TokenJPA {
	@Id
	@GeneratedValue
	@Column(name="idtoken")
	public Long id;

	@Column(name = "key", unique = true)
	public String token;

	@Enumerated(EnumType.STRING)
	@Column(name="type")
	public TokenType tokenType = TokenType.BEARER;
	
	@Column(name="revoked")
	public boolean revoked;

	@Column(name="expired")
	public boolean expired;

	@ManyToOne
	@JoinColumn(name = "iduser")
	@JsonIgnore
	public UserJPA user;

	public TokenJPA() {}
	
	public TokenJPA(TokenDTO token) {
		this.id = token.getId();
		this.user = new UserJPA(token.getUser());
		this.token = token.getToken();
		this.tokenType = token.getTokenType();
		this.expired = token.isExpired();
		this.revoked = token.isRevoked();
		
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

	public UserJPA getUser() {
		return user;
	}

	public void setUser(UserJPA user) {
		this.user = user;
	}
}
