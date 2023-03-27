package project.br.useAuthentication.jpaModel;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.interfaces.PasswordValidationConstraint;

@Entity
@Table(name = "usuario")
@SuppressWarnings("serial")
public class UserJPA implements UserDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_sequence")
	@SequenceGenerator(name="user_sequence", sequenceName="user_seq",  allocationSize = 1, initialValue = 4)
	@Column(name="iduser")
	private Long _id;
	
	@Column(name="username")
	@NotBlank(message="Name: Campo obrigatório")
	private String _username;
	
	@Column(name="email", unique=true)
	@NotNull(message="Email: Campo obrigatório")
	@Email(message="Email: Please provide a valid address", regexp="[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	private String _email;
	
	@Column(name="password")
	@PasswordValidationConstraint
	@NotBlank(message="Password: Campo obrigatório")
	private String _password;
	
	@Temporal(TemporalType.DATE)
	@Column(name="datanasc")
	@Past(message = "Birthdate should be in the past")
	@NotNull(message="Date: Campo obrigatório")
	private Date _datanasc;
	
	@JsonIgnore
	@OneToOne(mappedBy = "_user", cascade=CascadeType.ALL, orphanRemoval=true)
	private TokenJPA _token;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="User_Roles", 
	joinColumns= @JoinColumn(name="user_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<RoleJPA> _roles;
	
	public UserJPA() {
		
	}
	
	public UserJPA(UserDTO user) {
		this._id = user.getId();
		this._username = user.getUsername();
		this._email = user.getEmail();
		this._datanasc = user.getDatanasc();
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return this._roles;
	}
	public boolean isAccountNonExpired() {
		return true;
	}
	public boolean isAccountNonLocked() {
		return true;
	}
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public boolean isEnabled() {
		return true;
	}
			
	public TokenJPA getToken() {
		return this._token;
	}
	public void setToken(TokenJPA token) {
		this._token = token;
	}
	public Set<RoleJPA> getRoles() {
		return this._roles;
	}
	public void setRoles(Set<RoleJPA> roles) {
		this._roles = roles;
	}
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		this._id = id;
	}
	public String getUsername() {
		return this._username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	public String getPassword() {
		return this._password;
	}
	public void setPassword(String password) {
		this._password = password;
	}
	public String getEmail() {
		return this._email;
	}
	public void setEmail(String email) {
		this._email = email;
	}
	public Date getDatanasc() {
		return this._datanasc;
	}
	public void setDatanasc(Date datanasc) {
		this._datanasc = datanasc;
	}
}
