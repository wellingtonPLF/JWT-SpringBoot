package project.br.useAuthentication.dtoModel;

import java.util.Date;

import project.br.useAuthentication.jpaModel.UserJPA;

public class UserDTO {

	private Long id;
	private String name;
	private String password;
	private String email;
	private Date datanasc;
	
	public UserDTO() {
		
	}
	
	public UserDTO(Long id, String name, String password, String email, Date datanasc) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.datanasc = datanasc;
	}
	
	public UserDTO(UserJPA user) {
		this.id = user.getId();
		this.name = user.getName();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.datanasc = user.getDatanasc();
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

	@Override
	public String toString() {
		return String.format("Id:%d\n"
				+ "Name:%s\n"
				+ "Password:%s\n"
				+ "Email:%s\n"
				+ "DataNasc:%s\n", this.id, this.name, this.password, this.email, this.datanasc);
	}
}
