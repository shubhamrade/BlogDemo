package com.example.demo.response;

import java.util.List;

public class BlogResponse extends BaseResponse {

	Integer profileId;
	String userName;
	List<BlogListResponse> blogResponse;

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<BlogListResponse> getBlogResponse() {
		return blogResponse;
	}

	public void setBlogResponse(List<BlogListResponse> blogResponse) {
		this.blogResponse = blogResponse;
	}

}
