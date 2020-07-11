package com.example.demo.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.helper.StringHelper;
import com.example.demo.model.Blog;
import com.example.demo.model.Article;
import com.example.demo.model.Profile;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BlogRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.request.ArticleRequest;
import com.example.demo.request.BlogRequest;
import com.example.demo.response.ArticleListResponse;
import com.example.demo.response.ArticleResponse;
import com.example.demo.response.BlogListResponse;
import com.example.demo.response.BlogResponse;
import com.example.demo.response.ErrorEntity;
import com.example.demo.service.BlogManagementService;

@Service("BlogManagementService")
@Transactional
public class BlogManagementServiceImpl implements BlogManagementService {

	@Autowired
	BlogRepository blogRepository;

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	ArticleRepository articleRepository;

	Logger logger = LoggerFactory.getLogger("BlogManagementServiceImpl");

	/**
	 * add blog for specific user .
	 * 
	 * @param BlogRequest
	 * @return BlogResponse
	 */
	@Override
	public BlogResponse addBlog(BlogRequest request) {

		ArrayList<ErrorEntity> errorList = new ArrayList<>();
		BlogResponse response = new BlogResponse();
		Profile profile = profileRepository.findByProfileId(request.getUserId());
		if (StringHelper.isObjectNull(profile)) {
			logger.info("USER NOT FOUND");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(1));
			response.setErrors(errorList);
			return response;
		}
		Blog blog = blogRepository.findByProfileIdAndBlogNameIgnoreCase(profile.getProfileId(), request.getBlogName());
		if (StringHelper.isObjectNull(blog)) {
			blog = new Blog();
			this.prepareBlogData(blog, request, profile);
			blog = blogRepository.save(blog);

			if (StringHelper.isNullOrZero(blog.getBlogId())) {
				logger.info("blog is not inserted");
				response.setHasErrors(true);
				errorList.add(StringHelper.getErrorEntityFromCode(6));
				response.setErrors(errorList);
				return response;
			}

			this.prepareBlogResponse(response, profile);
			logger.info("blog is created");

		} else {

			logger.info("dupliacate blog name");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(2));
			response.setErrors(errorList);
			return response;

		}

		return response;

	}

	private void prepareBlogData(Blog blog, BlogRequest request, Profile profile) {

		blog.setBlogName(request.getBlogName());
		blog.setBlogDesc(request.getBlogDescription());
		blog.setProfileId(profile.getProfileId());
		Timestamp timestamp = new Timestamp(new Date().getTime());
		if (StringHelper.isNullOrZero(blog.getBlogId())) {
			blog.setCreatedDate(timestamp);

		}
		blog.setUpdatedDate(timestamp);

	}

	/**
	 * get all blog present in Database for specific user .
	 * 
	 * @param Integer
	 * @return BlogResponse
	 */
	@Override
	public BlogResponse getBlogs(Integer userId) {

		ArrayList<ErrorEntity> errorList = new ArrayList<>();
		BlogResponse response = new BlogResponse();
		Profile profile = profileRepository.findByProfileId(userId);
		if (!StringHelper.isObjectNull(profile)) {
			this.prepareBlogResponse(response, profile);
		} else {
			logger.info("USER NOT FOUND");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(1));
			response.setErrors(errorList);

		}
		return response;
	}

	/**
	 * prepare object for response (common use for add/update/delete/get blog)
	 * 
	 * @param response
	 * @param profile
	 */
	private void prepareBlogResponse(BlogResponse response, Profile profile) {
		response.setProfileId(profile.getProfileId());
		response.setUserName(profile.getUserName());

		List<Blog> blogList = blogRepository.findByProfileId(profile.getProfileId());

		List<BlogListResponse> blogResponseList = new ArrayList<>();
		if (!StringHelper.isObjectNull(blogList) && !blogList.isEmpty()) {

			blogList.forEach(blog -> {

				BlogListResponse blogResponse = new BlogListResponse();
				blogResponse.setBlogId(blog.getBlogId());
				blogResponse.setBlogName(blog.getBlogName());
				blogResponse.setBlogDescirption(blog.getBlogDesc());
				blogResponse.setCreatedDate(StringHelper.getStringDateFromTimestamp(blog.getCreatedDate()));
				blogResponse.setUpdatedDate(StringHelper.getStringDateFromTimestamp(blog.getUpdatedDate()));
				blogResponseList.add(blogResponse);

			});

		}
		response.setBlogResponse(blogResponseList);

	}

	/**
	 * update blog present in Database for user .
	 * 
	 * @param BlogRequest
	 * @return BlogResponse
	 */
	@Override
	public BlogResponse updateBlog(BlogRequest request) {
		BlogResponse response = new BlogResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		Blog bloginDb = blogRepository.findByBlogId(request.getBlogId());
		if (!StringHelper.isObjectNull(bloginDb)) {

			Blog blog = blogRepository.findByProfileIdAndBlogNameIgnoreCase(bloginDb.getProfileId(),
					request.getBlogName());

			if (StringHelper.isObjectNull(blog) || blog.getBlogId().equals(bloginDb.getBlogId())) {

				Profile profile = profileRepository.findByProfileId(bloginDb.getProfileId());

				this.prepareBlogData(bloginDb, request, profile);
				blogRepository.save(bloginDb);

				this.prepareBlogResponse(response, profile);
				logger.info("blog is created");

			} else {

				logger.info("dupliacate blog name");
				response.setHasErrors(true);
				errorList.add(StringHelper.getErrorEntityFromCode(2));
				response.setErrors(errorList);
				return response;

			}

		} else {
			logger.error("BLOG NOT FOUND");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);
		}

		return response;

	}

	/**
	 * delete blog and their all post present in Database for user .
	 * 
	 * @param Integer
	 * @return BlogResponse
	 */
	@Override
	public BlogResponse deleteBlog(Integer blogId) {

		BlogResponse response = new BlogResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		Blog bloginDb = blogRepository.findByBlogId(blogId);
		if (!StringHelper.isObjectNull(bloginDb)) {

			try {
				Profile profile = profileRepository.findByProfileId(bloginDb.getProfileId());
				articleRepository.deleteByBlogId(bloginDb.getBlogId());
				blogRepository.delete(bloginDb);
				prepareBlogResponse(response, profile);

			} catch (Exception e) {

				logger.error("exception : blog deletion failed");
				response.setHasErrors(true);
				errorList.add(StringHelper.getErrorEntityFromCode(5));
				response.setErrors(errorList);
				return response;

			}

		} else {
			logger.error("BLOG NOT FOUND");
			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			response.setErrors(errorList);
		}

		return response;
	}

	/**
	 * add Article (blog post ) for blog
	 * 
	 * @param ArticleRequest
	 * @return ArticleResponse
	 */
	@Override
	public ArticleResponse addArticle(ArticleRequest request) {

		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		ArticleResponse blogPostResponse = new ArticleResponse();
		Blog blog = blogRepository.findByBlogId(request.getBlogId());
		if (!StringHelper.isObjectNull(blog)) {
			Article post = new Article();
			prepareArticleData(post, request, blog);
			post = articleRepository.save(post);
			if (StringHelper.isNullOrZero(post.getArticleId())) {
				logger.info("blog is not inserted");
				blogPostResponse.setHasErrors(true);
				errorList.add(StringHelper.getErrorEntityFromCode(6));
				blogPostResponse.setErrors(errorList);
				return blogPostResponse;
			}

			logger.info("article is created");
			this.prepareArticleResponse(blogPostResponse, blog);

		} else {

			logger.error("BLOG NOT FOUND");
			blogPostResponse.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(0));
			blogPostResponse.setErrors(errorList);
		}

		return blogPostResponse;
	}

	private void prepareArticleResponse(ArticleResponse blogPostResponse, Blog blog) {

		blogPostResponse.setBlogId(blog.getBlogId());
		blogPostResponse.setBlogDescirption(blog.getBlogDesc());
		blogPostResponse.setBlogName(blog.getBlogName());

		List<Article> articleList = articleRepository.findByBlogId(blog.getBlogId());

		List<ArticleListResponse> articleResponseList = new ArrayList<>();
		articleList.forEach(article -> {

			ArticleListResponse articleResponse = new ArticleListResponse();
			articleResponse.setArticleId(article.getArticleId());
			articleResponse.setArticleText(article.getArticleText());
			articleResponse.setArticleTitle(article.getArticleTitle());
			articleResponse.setCreatedDate(StringHelper.getStringDateFromTimestamp(blog.getCreatedDate()));
			articleResponse.setUpdatedDate(StringHelper.getStringDateFromTimestamp(blog.getUpdatedDate()));

			articleResponseList.add(articleResponse);

		});

		blogPostResponse.setArticles(articleResponseList);

	}

	private void prepareArticleData(Article post, ArticleRequest request, Blog blog) {

		post.setArticleTitle(request.getArticleTitle());
		post.setArticleText(request.getArticleText());
		Timestamp timestamp = new Timestamp(new Date().getTime());
		if (StringHelper.isNullOrZero(post.getArticleId())) {
			post.setCreatedDate(timestamp);
		}
		post.setUpdatedDate(timestamp);
		post.setBlogId(blog.getBlogId());

	}

	/**
	 * update Article (blog post ) for blog
	 * 
	 * @param ArticleRequest
	 * @return ArticleResponse
	 */
	@Override
	public ArticleResponse updateArticle(ArticleRequest request) {

		ArticleResponse response = new ArticleResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		Article post = articleRepository.findByArticleId(request.getArticleId());

		if (!StringHelper.isObjectNull(post)) {

			Blog blog = blogRepository.findByBlogId(post.getBlogId());
			prepareArticleData(post, request, blog);
			articleRepository.save(post);

			this.prepareArticleResponse(response, blog);

		} else {

			logger.error("ARTICLE NOT FOUND");

			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(4));
			response.setErrors(errorList);
		}

		return response;
	}

	/**
	 * Delete article of specific article
	 * 
	 * @param Integer
	 * @return ArticleResponse
	 */
	@Override
	public ArticleResponse deleteArticle(Integer articleId) {

		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		ArticleResponse response = new ArticleResponse();
		Article post = articleRepository.findByArticleId(articleId);
		if (!StringHelper.isObjectNull(post)) {
			try {
				Blog blog = blogRepository.findByBlogId(post.getBlogId());
				articleRepository.delete(post);
				prepareArticleResponse(response, blog);

			} catch (Exception e) {
				logger.error("Exception :{}", e.getMessage());

				response.setHasErrors(true);
				errorList.add(StringHelper.getErrorEntityFromCode(5));
				response.setErrors(errorList);
			}

		} else {

			logger.error("ARTICLE NOT FOUND");

			response.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(4));
			response.setErrors(errorList);

		}
		return response;

	}

	/**
	 * get all articles of specific blog
	 * 
	 * @param Integer
	 * @return ArticleResponse
	 */
	@Override
	public ArticleResponse getArticles(Integer blogId) {
		ArticleResponse blogPostResponse = new ArticleResponse();
		ArrayList<ErrorEntity> errorList = new ArrayList<>();

		Blog blog = blogRepository.findByBlogId(blogId);
		if (!StringHelper.isObjectNull(blog)) {

			this.prepareArticleResponse(blogPostResponse, blog);

		} else {

			logger.error("BLOG NOT FOUND");
			blogPostResponse.setHasErrors(true);
			errorList.add(StringHelper.getErrorEntityFromCode(3));
			blogPostResponse.setErrors(errorList);
		}

		return blogPostResponse;
	}

}
