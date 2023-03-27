package project.br.useAuthentication.dtoModel;

import java.util.Date;

import project.br.useAuthentication.jpaModel.UserJPA;

public class UserDTO  {

	private Long _id;
	private String _name;
	private String _email;
	private Date _datanasc;
	
	public UserDTO() {
		
	}
	
	public UserDTO(Long id, String name, String email, Date datanasc) {
		this._id = id;
		this._name = name;
		this._email = email;
		this._datanasc = datanasc;
	}
	
	public UserDTO(UserJPA user) {
		this._id = user.getId();
		this._name = user.getUsername();
		this._email = user.getEmail();
		this._datanasc = user.getDatanasc();
	}
	
	public Long getId() {
		return this._id;
	}
	public void setId(Long id) {
		this._id = id;
	}
	public String getUsername() {
		return this._name;
	}
	public void setName(String name) {
		this._name = name;
	}
	public Date getDatanasc() {
		return this._datanasc;
	}
	public void setDatanasc(Date datanasc) {
		this._datanasc = datanasc;
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
				+ "Name:%s\n"
				+ "Email:%s\n"
				+ "DataNasc:%s\n", this._id, this._name, this._email, this._datanasc);
	}
}
