package project.br.useAuthentication.enumState;

public enum TokenEnum {
	ACCESS_TOKEN(1),
	REFRESH_TOKEN(2);

	private Integer value;

    TokenEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
