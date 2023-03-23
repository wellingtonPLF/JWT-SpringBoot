package project.br.useAuthentication.format;

public class ErrorResponseResult<T> {
	
	private T _error;
	private Integer _status;
		
	public ErrorResponseResult(T error, Integer status) {
		super();
		this._error= error;
		this._status = status;
	}

	public T getError() {
		return _error;
	}

	public void setError(T error) {
		this._error = error;
	}		

	public Integer getStatus() {
		return _status;
	}

	public void setStatus(Integer status) {
		this._status = status;
	}
	
}
