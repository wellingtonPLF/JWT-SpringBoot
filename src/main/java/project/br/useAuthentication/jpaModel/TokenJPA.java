package project.br.useAuthentication.jpaModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	public Long id;

	@OneToOne
	@JoinColumn(name = "iduser", unique = true)
	//@JsonManagedReference(value="template_Janela_Compoe")
	//@JsonIgnore
	public UserJPA user;
	
	@Column(name = "key", unique = true)
	public String token;

	@Enumerated(EnumType.STRING)
	@Column(name="type")
	public TokenType tokenType = TokenType.BEARER;

	public TokenJPA() {}
	
	public TokenJPA(String token, TokenType tokenType, UserJPA user) {
		this.token = token;
		this.tokenType = tokenType;
		this.user = user;
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

	public UserJPA getUser() {
		return user;
	}

	public void setUser(UserJPA user) {
		this.user = user;
	}
}
