package project.br.useAuthentication.jpaModel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import project.br.useAuthentication.dtoModel.UserDTO;

@Entity
@Table(name = "usuario")
public class UserJPA {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_sequence")
	@SequenceGenerator(name="user_sequence", sequenceName="user_seq",  allocationSize = 1, initialValue = 4)
	@Column(name="user_ID")
	private Long _id;
	
	@NotBlank(message="Name: Campo obrigatório")
	@Column(name="nickname")
	private String _nickName;
	
	@Past(message = "Birthdate should be in the past")
	@NotNull(message="Date: Campo obrigatório")
	@Temporal(TemporalType.DATE)
	@Column(name="borndate")
	private Date _bornDate;

	@OneToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "auth_ID", unique = true)
	private AuthJPA _auth;
	
	public UserJPA() {
		
	}
	
	public UserJPA(UserDTO user) {
		this._id = user.getId();
		this._nickName = user.getNickName();
		this._bornDate= user.getBornDate();
	}
		
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		this._id = id;
	}
	public String getNickName() {
		return this._nickName;
	}
	public void setNickName(String nickName) {
		this._nickName = nickName;
	}
	public Date getBornDate() {
		return this._bornDate;
	}
	public void setBornDate(Date bornDate) {
		this._bornDate = bornDate;
	}
	public AuthJPA getAuth() {
		return this._auth;
	}
	public void setAuth(AuthJPA auth) {
		this._auth = auth;
	}	
}

