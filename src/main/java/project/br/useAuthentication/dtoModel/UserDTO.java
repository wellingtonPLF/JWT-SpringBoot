package project.br.useAuthentication.dtoModel;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import project.br.useAuthentication.jpaModel.RoleJPA;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.jpaModel.UserJPA;

@SuppressWarnings("serial")
public class UserDTO implements UserDetails {

	private Long id;
	private String name;
	private String email;
	private Date datanasc;
	private String password;
	private Set<RoleJPA> roles;
	private List<TokenJPA> tokens;
	
	public UserDTO() {
		
	}
	
	public UserDTO(Long id, String name, String password, String email, Date datanasc, Set<RoleJPA> roles, List<TokenJPA> tokens) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.datanasc = datanasc;
		this.roles = roles;
		this.tokens = tokens;
	}
	
	public UserDTO(UserJPA user) {
		this.id = user.getId();
		this.name = user.getUsername();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.datanasc = user.getDatanasc();
		this.roles = user.getRoles();
		this.tokens = user.getTokens();
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return this.roles;
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getDatanasc() {
		return datanasc;
	}
	public void setDatanasc(Date datanasc) {
		this.datanasc = datanasc;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<RoleJPA> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleJPA> roles) {
		this.roles = roles;
	}
	public List<TokenJPA> getTokens() {
		return tokens;
	}
	public void setTokens(List<TokenJPA> tokens) {
		this.tokens = tokens;
	}

	@Override
	public String toString() {
		return String.format("Id:%d\n"
				+ "Name:%s\n"
				+ "Password:%s\n"
				+ "Email:%s\n"
				+ "DataNasc:%s\n", this.id, this.name, this.password, this.email, this.datanasc);
	}
}
