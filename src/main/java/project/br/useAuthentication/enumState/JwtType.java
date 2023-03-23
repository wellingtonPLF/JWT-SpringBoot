package project.br.useAuthentication.enumState;

public enum JwtType {
	INVALID_USER("Invalid User!"),
	INVALID_AT("Access Token is not valid, you must SignIn!"),
	INVALID_RT("Refresh Token is not valid, you must SignIn!"),
	EXPIRED_AT("Expired Access Token!"),
	EXPIRED_RT("Expired Refresh Token!");
	
	private String value;

    JwtType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
