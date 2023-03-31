package project.br.useAuthentication.format;

public class ErrorResult<T> {
	
	private T _error;
	private Enum<?> _type;
	private Integer _status;
	
	public ErrorResult(T error, Enum<?> type, Integer status) {
		super();
		this._error= error;
		this._type = type;
		this._status = status;
	}
	
	public ErrorResult(Integer status) {
		super();
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

	public Enum<?> getType() {
		return _type;
	}

	public void setType(Enum<?> _type) {
		this._type = _type;
	}
	
}
