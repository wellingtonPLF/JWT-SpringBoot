package project.br.useAuthentication.dtoModel;

import java.util.Date;

import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.jpaModel.UserJPA;

public class UserDTO  {

	private Long _id;
	private String _name;
	private String _email;
	private Date _bornDate;
	
	public UserDTO() {
		
	}
	
	public UserDTO(Long id, String name, String email, Date bornDate) {
		this._id = id;
		this._name = name;
		this._email = email;
		this._bornDate = bornDate;
	}
	
	public UserDTO(UserJPA user, AuthJPA auth) {
		this._id = user.getId();
		this._name = user.getNickName();
		this._email = auth.getEmail();
		this._bornDate = user.getBornDate();
	}
	
	public Long getId() {
		return this._id;
	}
	public void setId(Long id) {
		this._id = id;
	}
	public String getNickName() {
		return this._name;
	}
	public void setNickName(String name) {
		this._name = name;
	}
	public Date getBornDate() {
		return this._bornDate;
	}
	public void setBornDate(Date bornDate) {
		this._bornDate = bornDate;
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
				+ "BornDate:%s\n", this._id, this._name, this._email, this._bornDate);
	}
}
