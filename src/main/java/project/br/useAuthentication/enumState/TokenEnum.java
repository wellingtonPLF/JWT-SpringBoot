package project.br.useAuthentication.enumState;

public enum TokenEnum {
	ACCESS_TOKEN(1),
	REFRESH_TOKEN(24 * 7);

	private Integer value;

    TokenEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
