package project.br.useAuthentication.enumState;

public enum TokenType {
	ACCESS_TOKEN(1),
	REFRESH_TOKEN(24 * 7);

	private Integer value;

    TokenType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
