package com.example.demo.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Errors {

	@SerializedName(value = "Errors")
	private ArrayList<ErrorEntity> errors;

	public Errors(ArrayList<ErrorEntity> errors) {
		super();
		this.errors = errors;
	}

	public ArrayList<ErrorEntity> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<ErrorEntity> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "Errors [errors=" + errors + "]";
	}
}
