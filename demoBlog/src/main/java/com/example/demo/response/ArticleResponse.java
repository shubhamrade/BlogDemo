package com.example.demo.response;

import java.util.List;

public class ArticleResponse extends BaseResponse {

	Integer blogId;
	String blogName;
	String blogDescirption;
	List<ArticleListResponse> articles;

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

	public List<ArticleListResponse> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleListResponse> articles) {
		this.articles = articles;
	}

}
