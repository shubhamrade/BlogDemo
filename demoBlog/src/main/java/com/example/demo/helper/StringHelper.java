package com.example.demo.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.util.ResourceUtils;

import com.example.demo.response.ErrorEntity;
import com.example.demo.response.Errors;
import com.google.gson.Gson;

public class StringHelper {

	/**
	 * This method will check provided object is null or not
	 * 
	 * @param a
	 * @return
	 */
	public static boolean isObjectNull(Object obj) {

		return (obj == null);
	}

	/**
	 * This method will check given string value is Null or Empty
	 * 
	 * @param s String value.
	 * @return boolean value
	 */
	public static boolean isNullOrEmpty(String s) {
		boolean status = false;
		if (null == s) {
			return true;
		}
		if (s.isEmpty() || s.equalsIgnoreCase("NULL")) {
			status = true;
		}

		if (s.isEmpty() || s.equalsIgnoreCase("")) {
			status = true;
		}

		return status;
	}

	/**
	 * This method will check provided integer is null or not
	 * 
	 * @param num integer value
	 * @return
	 */
	public static boolean isNullOrZero(Integer num) {
		boolean status = false;
		if (null == num || num == 0)
			status = true;
		return status;
	}

	public static ErrorEntity getErrorEntityFromCode(int errorId) {
		Errors errorsList = getErrorsListFromJSONFile();
		if (errorsList != null) {

			ArrayList<ErrorEntity> errors = errorsList.getErrors();

			Optional<ErrorEntity> error = errors.stream().filter(errorobj -> errorobj.getErrorId().equals(errorId))
					.findFirst();
//			for (ErrorEntity error : errors) {
//				if (error.getErrorId() == errorId) {
//					return error;
//				}
//			}
			if (error.isPresent()) {
				return error.get();
			}
		}
		return null;
	}

	public static Errors getErrorsListFromJSONFile() {
		Errors errorsList = null;
		Gson gson = new Gson();

		File file;
		try {

			file = ResourceUtils.getFile("classpath:" + "error.json");

			String content = new String(Files.readAllBytes(file.toPath()));

			errorsList = gson.fromJson(content, Errors.class);

		} catch (IOException e) {
			System.out.println("Exception!:" + e);
		}
		return errorsList;
	}

	
	/**
	 * This method  convert given date from timestamp to string 
	 * 
	 * @param Timestamp
	 * @return String
	 */
	public static String getStringDateFromTimestamp(Timestamp timestamp) {
		Date date = new Date();
		if (!StringHelper.isObjectNull(timestamp)) {
			date.setTime(timestamp.getTime());
			return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
		}

		return null;
	}
}
