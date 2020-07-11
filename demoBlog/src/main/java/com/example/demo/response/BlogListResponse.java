package com.example.demo.response;

public class BlogListResponse {

	Integer blogId;
	String blogName;
	String blogDescirption;
	String createdDate;
	String updatedDate;

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

	public String getBlogDescirption() {
		return blogDescirption;
	}

	public void setBlogDescirption(String blogDescirption) {
		this.blogDescirption = blogDescirption;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

}
