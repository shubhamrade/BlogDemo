package com.example.demo.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse {

	@JsonProperty("HasErrors")
	private boolean hasErrors;

	@JsonProperty("Errors")
	private ArrayList<ErrorEntity> errors;

	public boolean isHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public ArrayList<ErrorEntity> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<ErrorEntity> errors) {
		this.errors = errors;
	}

}
