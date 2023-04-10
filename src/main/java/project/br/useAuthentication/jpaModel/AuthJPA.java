package project.br.useAuthentication.jpaModel;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import project.br.useAuthentication.interfaces.PasswordValidationConstraint;

@Entity
@Table(name = "auth")
public class AuthJPA implements UserDetails {

	private final static long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_sequence")
	@SequenceGenerator(name="user_sequence", sequenceName="user_seq",  allocationSize = 1, initialValue = 4)
	@Column(name="auth_ID")
	private Long _id;
	
	@Column(name="username")
	private String _username;
	
	@Email(message="Email: Please provide a valid address", regexp="[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	@Column(name="email", unique=true)
	private String _email;
	
	@PasswordValidationConstraint
	@NotBlank(message="Password: Campo obrigatório")
	@Column(name="password")
	private String _password;
	
	@OneToOne(mappedBy = "_auth", cascade=CascadeType.ALL, orphanRemoval=true)
	private UserJPA _user;
	
	@OneToOne(mappedBy = "_auth", cascade=CascadeType.ALL, orphanRemoval=true)
	private TokenJPA _token;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(name="Auth_Roles",
	joinColumns= @JoinColumn(name="auth_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<RoleJPA> _roles;
	
	public AuthJPA() {}
	
	public AuthJPA(String username, String email, String password) {
		this._username = username;
		this._email = email;
		this._password = password;
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
	//-----------------------------------------------
	public Set<RoleJPA> getRoles() {
		return this._roles;
	}
	public void setRoles(Set<RoleJPA> roles) {
		this._roles = roles;
	}
	//-----------------------------------------------
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
	
	@Override
	public String toString() {
		return String.format("Id:%d\n"
				+ "UserName:%s\n"
				+ "Password:%s\n"
				+ "Email:%s\n", this._id, this._username, this._password, this._email);
	}
}
