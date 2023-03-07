package project.br.useAuthentication.dtoModel;

import java.util.Date;
import java.util.List;
import java.util.Set;

import project.br.useAuthentication.jpaModel.RoleJPA;
import project.br.useAuthentication.jpaModel.UserJPA;

public class UserDTO {

	private Long id;
	private String name;
	private String password;
	private String email;
	private Date datanasc;
	private Set<RoleJPA> roles;
	
	public UserDTO() {
		
	}
	
	public UserDTO(Long id, String name, String password, String email, Date datanasc, Set<RoleJPA> roles) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.datanasc = datanasc;
		this.roles = roles;
	}
	
	public UserDTO(UserJPA user) {
		this.id = user.getId();
		this.name = user.getUsername();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.datanasc = user.getDatanasc();
		this.roles = user.getRoles();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
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

	@Override
	public String toString() {
		return String.format("Id:%d\n"
				+ "Name:%s\n"
				+ "Password:%s\n"
				+ "Email:%s\n"
				+ "DataNasc:%s\n", this.id, this.name, this.password, this.email, this.datanasc);
	}
}
