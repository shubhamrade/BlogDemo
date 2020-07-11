package com.example.demo.service;

import com.example.demo.request.ArticleRequest;
import com.example.demo.request.BlogRequest;
import com.example.demo.response.ArticleResponse;
import com.example.demo.response.BlogResponse;

public interface BlogManagementService {

	BlogResponse addBlog(BlogRequest request);

	BlogResponse getBlogs(Integer userId);

	BlogResponse updateBlog(BlogRequest request);

	BlogResponse deleteBlog(Integer blogId);

	ArticleResponse addArticle(ArticleRequest request);

	ArticleResponse updateArticle(ArticleRequest request);

	ArticleResponse deleteArticle(Integer articleId);

	ArticleResponse getArticles(Integer blogid);

}
