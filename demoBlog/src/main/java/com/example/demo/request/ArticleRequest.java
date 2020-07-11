package com.example.demo.request;

import com.example.demo.helper.StringHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ArticleRequest {

	String articleTitle;
	String articleText;
	Integer articleId;

	@JsonIgnore
	Integer blogId;

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getArticleText() {
		return articleText;
	}

	public void setArticleText(String articleText) {
		this.articleText = articleText;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getBlogId() {
		return blogId;
	}

	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}

	@Override
	public String toString() {
		return "BlogPostRequest [articleTitle=" + articleTitle + ", articleText=" + articleText + ", articleId="
				+ articleId + "]";
	}

	public boolean isValid() {
		return (!StringHelper.isNullOrEmpty(articleText) && !StringHelper.isNullOrEmpty(articleTitle));
	}

}
