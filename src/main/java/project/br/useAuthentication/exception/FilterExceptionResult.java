package project.br.useAuthentication.exception;

import project.br.useAuthentication.enumState.JwtType;

public class FilterExceptionResult extends RuntimeException {
	private final static long serialVersionUID = 1L;
	
	private JwtType errorCode;

    public FilterExceptionResult(JwtType errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }

    public JwtType getErrorCode() {
        return errorCode;
    }
}
