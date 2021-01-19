package com.lz.files.common;

import com.lz.files.utils.JsonUtil;

import java.io.Serializable;
import java.util.Map;


public class ResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private Object data;
	private String message;

	public String getCode() {
		return code == null ? "" : code.trim();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T getData(Class<T> clz) {
		if (data instanceof Map) {
			return JsonUtil.fromMap((Map) data, clz);
		}

		return (T) data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResponseDto [code=" + code + ", data=" + data + ", message=" + message + "]";
	}

	public ResponseDto(String code, Object data, String message) {
		super();
		this.code = code;
		this.data = data;
		this.message = message;
	}

	public ResponseDto() {
		super();
	}

}
