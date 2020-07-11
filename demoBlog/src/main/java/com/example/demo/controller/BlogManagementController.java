package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.helper.StringHelper;
import com.example.demo.request.ArticleRequest;
import com.example.demo.request.BlogRequest;
import com.example.demo.response.ArticleResponse;
import com.example.demo.response.BlogResponse;
import com.example.demo.response.ErrorEntity;
import com.example.demo.service.BlogManagementService;

@RestController
public class BlogManagementController {

	@Autowired
	BlogManagementService blogManagementService;

	Logger logger = LoggerFactory.getLogger("BlogManagementController");

	/**
	 * API Endpoint - This endpoint retrive all blog of given User
	 * 
	 * @param userId (path veriable)
	 * @return BlogResponse
	 */
	@GetMapping(value = "/data/{userId}/blog")
	public BlogResponse getBlogs(@PathVariable Integer userId) {

		BlogResponse response = new BlogResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();
		logger.info("blog get request");

		if (!StringHelper.isNullOrZero(userId)) {
			response = blogManagementService.getBlogs(userId);
		} else {
			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);
		}

		return response;

	}

	/**
	 * API Endpoint - This endpoint add blog for given user
	 * 
	 * @param userId      (path veriable)
	 * @param BlogRequest BlogRequest object
	 * @return BlogResponse
	 */

	@PostMapping(value = "/data/{userId}/blog")
	public BlogResponse addBlog(@PathVariable Integer userId, @RequestBody BlogRequest request) {

		BlogResponse response = new BlogResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		if (request.isValid()) {

			request.setUserId(userId);
			response = blogManagementService.addBlog(request);

		} else {
			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);
		}

		return response;

	}

	/**
	 * API Endpoint - This endpoint update blog which is present in database for
	 * given User
	 * 
	 * @param blogId  (path veriable)
	 * @param request BlogRequest object
	 * @return BlogResponse
	 */
	@PutMapping(value = "/data/{blogId}/blog")
	public BlogResponse updateBlog(@PathVariable Integer blogId, @RequestBody BlogRequest request) {

		BlogResponse response = new BlogResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		if (request.isValid()) {

			request.setBlogId(blogId);
			response = blogManagementService.updateBlog(request);

		} else {
			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);

		}
		return response;
	}

	/**
	 * API Endpoint - This endpoint delete given blog which is present in database
	 * 
	 * @param blogId Integer Object (path veriable)
	 * 
	 * @return BlogResponse
	 */
	@DeleteMapping(value = "/data/{blogId}/blog")
	public BlogResponse deleteBlog(@PathVariable Integer blogId) {
		BlogResponse response = new BlogResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		if (!StringHelper.isNullOrZero(blogId)) {

			response = blogManagementService.deleteBlog(blogId);

		} else {

			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);

		}
		return response;
	}

	/**
	 * API Endpoint - This endpoint retrive all the BlogPost of given Blog
	 * 
	 * @param blogId Integer Object (path veriable)
	 * 
	 * @return ArticleResponse
	 */
	@GetMapping(value = "/data/{blogid}/article")
	public ArticleResponse getBlogPost(@PathVariable Integer blogid) {
		ArticleResponse response = new ArticleResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		if (!StringHelper.isNullOrZero(blogid)) {

			response = blogManagementService.getArticles(blogid);

		} else {

			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);

		}
		return response;

	}

	/**
	 * API Endpoint - This endpoint add BlogPost of given blog
	 * 
	 * @param blogId  Integer Object (path veriable)
	 * @param request ArticleRequest object
	 * 
	 * @return ArticleResponse
	 */
	@PostMapping(value = "/data/{blogId}/article")
	public ArticleResponse addBlogPost(@PathVariable Integer blogId, @RequestBody ArticleRequest request) {

		ArticleResponse response = new ArticleResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();
		if (request.isValid()) {

			request.setBlogId(blogId);
			response = blogManagementService.addArticle(request);

		} else {

			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);

		}
		return response;

	}

	/**
	 * API Endpoint - This endpoint update BlogPost
	 * 
	 * @param articleId Integer Object (path veriable)
	 * @param request   ArticleRequest object
	 * 
	 * @return ArticleResponse
	 */
	@PutMapping(value = "/data/{articleId}/article")
	public ArticleResponse updateBlogPost(@PathVariable Integer articleId, @RequestBody ArticleRequest request) {
		System.out.println("updateBlog");
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		ArticleResponse response = new ArticleResponse();
		if (request.isValid()) {

			request.setArticleId(articleId);
			response = blogManagementService.updateArticle(request);

		} else {
			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);
		}

		return response;

	}

	/**
	 * API Endpoint - This endpoint delete BlogPost
	 * 
	 * @param articleId Integer Object (path veriable)
	 * 
	 * 
	 * @return ArticleResponse
	 */
	@DeleteMapping(value = "/data/{articleId}/article")
	public ArticleResponse deleteBlogPost(@PathVariable Integer articleId) {
		ArticleResponse response = new ArticleResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		if (!StringHelper.isNullOrZero(articleId)) {

			response = blogManagementService.deleteArticle(articleId);

		} else {
			logger.error("INVALID REQUEST");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);
		}

		return response;
	}

}
