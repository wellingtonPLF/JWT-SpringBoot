package project.br.useAuthentication.jpaModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import project.br.useAuthentication.dtoModel.UserDTO;

@Entity
@Table(name = "usuario")
public class UserJPA {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String password;
	private String datanasc;
	
	public UserJPA() {
		
	}
	
	public UserJPA(UserDTO user) {
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
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatanasc() {
		return this.datanasc;
	}
	public void setDatanasc(String datanasc) {
		this.datanasc = datanasc;
	}
}
