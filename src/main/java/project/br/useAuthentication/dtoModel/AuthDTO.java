package project.br.useAuthentication.dtoModel;

public class AuthDTO {

	private String _email;
	private String _password;
	
	public AuthDTO() {}
	
	public AuthDTO(String email, String password) {
		this._email = email;
		this._password = password;
	}
	
	public String getEmail() {
		return this._email;
	}
	public void setEmail(String email) {
		this._email = email;
	}
	public String getPassword() {
		return this._password;
	}
	public void setPassword(String password) {
		this._password = password;
	}	
}