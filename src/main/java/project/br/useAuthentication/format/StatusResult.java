package project.br.useAuthentication.format;

public class StatusResult<T> {
	
	private Integer _status;
	private T _data;
	
	public StatusResult() {}
	
	public StatusResult(Integer status, T data) {
		this._status = status;
		this._data = data;
	}

	public Integer getStatus() {
		return _status;
	}

	public void setStatus(Integer _status) {
		this._status = _status;
	}

	public T getData() {
		return _data;
	}

	public void setData(T _data) {
		this._data = _data;
	}
}
