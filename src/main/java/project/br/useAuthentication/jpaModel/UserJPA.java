package project.br.useAuthentication.jpaModel;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import project.br.useAuthentication.dtoModel.UserDTO;

@Entity
@Table(name = "usuario")
public class UserJPA {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_sequence")
	@SequenceGenerator(name="user_sequence", sequenceName="user_seq",  allocationSize = 1, initialValue = 5)
	@Column(name="id")
	private Long _id;
	
	@Column(name="name")
	@NotBlank(message="Campo obrigatório") 
	private String _name;
	
	@Column(name="email", unique=true)
	@NotBlank(message="Campo obrigatório")
	@Email(message="Please provide a valid email address")
	private String _email;
	
	@NotBlank(message="Campo obrigatório")
	@Column(name="password")
	private String _password;
	
	@Temporal(TemporalType.DATE) 
	@Column(name="datanasc")
	private Date _datanasc;
	
	public UserJPA() {
		
	}
	
	public UserJPA(UserDTO user) {
		this._id = user.getId();
		this._name = user.getName();
		this._password = user.getPassword();
		this._datanasc = user.getDatanasc();
	}
	
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		this._id = id;
	}
	public String getName() {
		return this._name;
	}
	public void setName(String name) {
		this._name = name;
	}
	public String getPassword() {
		return this._password;
	}
	public void setPassword(String password) {
		this._password = password;
	}
	public Date getDatanasc() {
		return this._datanasc;
	}
	public void setDatanasc(Date datanasc) {
		this._datanasc = datanasc;
	}
}
