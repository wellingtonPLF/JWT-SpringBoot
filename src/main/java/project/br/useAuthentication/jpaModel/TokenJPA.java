package project.br.useAuthentication.jpaModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "token")
public class TokenJPA {
	@Id
	@GeneratedValue
	@Column(name="token_ID")
	private Long _id;

	@OneToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "auth_ID", unique = true)
	private AuthJPA _auth;
	
	@Column(name = "key", unique = true)
	private String _token;

	public TokenJPA() {}
	
	public TokenJPA(String token, AuthJPA auth) {
		this._token = token;
		this._auth = auth;
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

	public AuthJPA getAuth() {
		return this._auth;
	}

	public void setAuth(AuthJPA auth) {
		this._auth = auth;
	}
}
