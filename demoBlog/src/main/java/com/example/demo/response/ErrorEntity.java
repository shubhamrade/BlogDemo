package com.example.demo.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class ErrorEntity implements Serializable {

	/**
	 * 
	 */
	@JsonIgnore
	@SerializedName(value = "Id")
	private Integer errorId;

	@JsonProperty("Code")
	@SerializedName(value = "Code")
	private String errorCode;

	@JsonProperty("Message")
	@SerializedName(value = "Message")
	private String errorMessage;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ErrorEntity(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

}
