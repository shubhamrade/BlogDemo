package com.example.demo.request;

import com.example.demo.helper.StringHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class BlogRequest {

	private Integer blogId;
	private String blogName;
	private String blogDescription;

	@JsonIgnore
	private Integer userId;

	public Integer getBlogId() {
		return blogId;
	}

	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}

	public String getBlogName() {
		return blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	public String getBlogDescription() {
		return blogDescription;
	}

	public void setBlogDescription(String blogDescription) {
		this.blogDescription = blogDescription;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "BlogRequest [blogName=" + blogName + ", blog_description=" + blogDescription + "]";
	}

	public boolean isValid() {
		return (!StringHelper.isNullOrEmpty(blogName) && !StringHelper.isObjectNull(blogDescription));
	}

}
