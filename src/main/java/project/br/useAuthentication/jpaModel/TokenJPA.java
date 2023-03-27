package project.br.useAuthentication.jpaModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import project.br.useAuthentication.enumState.TokenType;

@Entity
@Table(name = "token")
public class TokenJPA {
	@Id
	@GeneratedValue
	@Column(name="idtoken")
	private Long _id;

	@OneToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "iduser", unique = true)
	private UserJPA _user;
	
	@Column(name = "key", unique = true)
	private String _token;

	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private TokenType _tokenType = TokenType.BEARER;

	public TokenJPA() {}
	
	public TokenJPA(String token, TokenType tokenType, UserJPA user) {
		this._token = token;
		this._tokenType = tokenType;
		this._user = user;
	}
	
	public Long getId() {
		return this._id;
	}

	public void setId(Long id) {
		this._id = id;
	}

	public String getToken() {
		return this._token;
	}

	public void setToken(String token) {
		this._token = token;
	}

	public TokenType getTokenType() {
		return this._tokenType;
	}

	public void setTokenType(TokenType tokenType) {
		this._tokenType = tokenType;
	}

	public UserJPA getUser() {
		return this._user;
	}

	public void setUser(UserJPA user) {
		this._user = user;
	}
}
