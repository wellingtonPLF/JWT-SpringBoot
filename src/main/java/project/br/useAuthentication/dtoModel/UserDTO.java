package project.br.useAuthentication.dtoModel;

import project.br.useAuthentication.jpaModel.UserJPA;

public class UserDTO {

	private Long id;
	private String name;
	private String password;
	private String datanasc;
	
	public UserDTO() {
		
	}
	
	public UserDTO(UserJPA user) {
		this.id = user.getId();
		this.name = user.getName();
		this.password = user.getPassword();
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
	public String getDatanasc() {
		return datanasc;
	}
	public void setDatanasc(String datanasc) {
		this.datanasc = datanasc;
	}
	
	@Override
	public String toString() {
		return String.format("Id:%d\n"
				+ "Name:%s\n"
				+ "Password:%s\n"
				+ "DataNasc:%s\n", this.id, this.name, this.password, this.datanasc);
	}
}
