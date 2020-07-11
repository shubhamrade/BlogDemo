package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.helper.StringHelper;
import com.example.demo.model.Article;
import com.example.demo.model.Blog;
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
import com.example.demo.service.BlogManagementService;
import com.example.demo.service.impl.BlogManagementServiceImpl;

@SpringBootTest
class BlogManagementServiceTest {

	@Mock
	BlogRepository blogRepository;

	@Mock
	ProfileRepository profileRepository;

	@Mock
	ArticleRepository articleRepository;

	@InjectMocks
	BlogManagementService blogManagementService = new BlogManagementServiceImpl();

	Profile getProfileData() {

		return new Profile(1, "TEST User", "test@gmail.com", null);
	}

	Blog getBlogObject() {
		return new Blog(1, 1, "blog test name", "blogtestnamedesc", null, null);
	}

	private Article getArticleObject() {
		return new Article(1, 1, "article Title", "articleText", null, null);
	}

	private List<Article> getArticleList() {
		List<Article> articleList = new ArrayList<>();
		articleList.add(new Article(1, 1, "article Title", "articleText", null, null));
		articleList.add(new Article(2, 1, "article Title2", "articleText2", null, null));
		return articleList;
	}

	private List<Blog> getBlogList() {
		List<Blog> blogList = new ArrayList<>();
		blogList.add(new Blog(1, 1, "blog test name", "blogtestnamedesc", null, null));
		blogList.add(new Blog(2, 1, "blog test name1", "blogtestnamedesc1", null, null));
		blogList.add(new Blog(2, 1, "blog test name3", "blogtestnamedesc3", null, null));
		return blogList;

	}

	public static Stream<BlogRequest> getDummyBlogRequest() {

		BlogRequest request = new BlogRequest();
		request.setBlogDescription("blogtestnamedesc");
		request.setBlogName("blog test name");
		request.setUserId(1);
		request.setBlogId(1);

		return Stream.of(request);

	}

	public static Stream<ArticleRequest> getDummyArticleRequest() {

		ArticleRequest request = new ArticleRequest();

		request.setBlogId(1);
		request.setArticleId(1);
		request.setArticleTitle("article Title");
		request.setArticleText("articleText");

		return Stream.of(request);

	}

	@ParameterizedTest
	@MethodSource("getDummyBlogRequest")
	public void testAddBlog_sucsess(BlogRequest request) {
		// GIVEN
		Blog ExpectedBlog = null;
		when(profileRepository.findByProfileId(Mockito.anyInt())).thenReturn(getProfileData());
		when(blogRepository.findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(ExpectedBlog);
		when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(getBlogObject());

		// WHEN
		Profile actualProfile = profileRepository.findByProfileId(request.getUserId());
		assertEquals(getProfileData(), actualProfile);

		Blog blog = blogRepository.findByProfileIdAndBlogNameIgnoreCase(actualProfile.getProfileId(),
				request.getBlogName());
		assertNull(blog, "no duplicate blog name");

		blog = new Blog();
		prepareBlogData(blog, request, actualProfile);
		blog = blogRepository.save(blog);
		assertEquals(getBlogObject(), blog);

		// THEN
		verify(profileRepository, times(1)).findByProfileId(Mockito.anyInt());
		verify(blogRepository, times(1)).findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString());
		verify(blogRepository, times(1)).save(Mockito.any(Blog.class));
	}

	@ParameterizedTest
	@MethodSource("getDummyBlogRequest")
	public void testAddBlog_IfProfileNotFound(BlogRequest request) {
		// GIVEN
		Blog ExpectedBlog = null;
		when(profileRepository.findByProfileId(Mockito.anyInt())).thenReturn(null);
		when(blogRepository.findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(ExpectedBlog);
		when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(getBlogObject());

		// WHEN
		Profile actualProfile = profileRepository.findByProfileId(request.getUserId());
		assertNull(actualProfile, "profile Not found");

		// THEN
		verify(profileRepository, times(1)).findByProfileId(Mockito.anyInt());
		verify(blogRepository, never()).findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString());
		verify(blogRepository, never()).save(Mockito.any(Blog.class));
	}

	@ParameterizedTest
	@MethodSource("getDummyBlogRequest")
	public void testAddBlog_IfBlogNameIsDuplicate(BlogRequest request) {
		// GIVEN
		when(profileRepository.findByProfileId(Mockito.anyInt())).thenReturn(getProfileData());
		when(blogRepository.findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(getBlogObject());
		when(blogRepository.save(Mockito.any(Blog.class))).thenReturn(getBlogObject());

		// WHEN
		Profile actualProfile = profileRepository.findByProfileId(request.getUserId());
		assertEquals(getProfileData(), actualProfile);

		Blog blog = blogRepository.findByProfileIdAndBlogNameIgnoreCase(actualProfile.getProfileId(),
				request.getBlogName());
		assertNotNull(blog);

		// THEN
		verify(profileRepository, times(1)).findByProfileId(Mockito.anyInt());
		verify(blogRepository, times(1)).findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString());
		verify(blogRepository, never()).save(Mockito.any(Blog.class));
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

	@ParameterizedTest
	@MethodSource("getDummyBlogRequest")
	public void testUpdateBlog_sucsess(BlogRequest request) {

		// GIVEN
		when(blogRepository.findByBlogId(Mockito.anyInt())).thenReturn(getBlogObject());
		when(blogRepository.findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(null);
		when(profileRepository.findByProfileId(Mockito.anyInt())).thenReturn(getProfileData());

		// WHEN

		Blog actaulBlog = blogRepository.findByBlogId(request.getBlogId());
		assertEquals(getBlogObject(), actaulBlog);

		Blog duplicateCheckblog = blogRepository.findByProfileIdAndBlogNameIgnoreCase(actaulBlog.getProfileId(),
				request.getBlogName());
		assertNull(duplicateCheckblog, "no duplicate blog name");

		Profile profile = profileRepository.findByProfileId(actaulBlog.getProfileId());
		assertEquals(getProfileData(), profile);
		prepareBlogData(actaulBlog, request, profile);
		blogRepository.save(actaulBlog);

		// THEN

		verify(profileRepository, times(1)).findByProfileId(Mockito.anyInt());
		verify(blogRepository, times(1)).findByProfileIdAndBlogNameIgnoreCase(Mockito.anyInt(), Mockito.anyString());
		verify(blogRepository, times(1)).save(Mockito.any(Blog.class));
		verify(blogRepository, times(1)).findByBlogId(Mockito.anyInt());

	}

	@ParameterizedTest
	@ValueSource(ints = { 1 })
	public void testDeleteBlog_sucsess(Integer blogId) {

		// GIVEN
		when(blogRepository.findByBlogId(Mockito.anyInt())).thenReturn(getBlogObject());

		// WHEN

		Blog actaulBlog = blogRepository.findByBlogId(blogId);
		assertEquals(getBlogObject(), actaulBlog);
		articleRepository.deleteByBlogId(actaulBlog.getBlogId());
		blogRepository.delete(actaulBlog);

		// THEN
		verify(blogRepository, times(1)).findByBlogId(Mockito.anyInt());
		verify(articleRepository, times(1)).deleteByBlogId(Mockito.anyInt());
		verify(blogRepository, times(1)).delete(Mockito.any(Blog.class));

	}

	@ParameterizedTest
	@ValueSource(ints = { 1 })
	public void testGetBlog_sucsess(Integer userId) {
		// GIVEN
		when(profileRepository.findByProfileId(Mockito.anyInt())).thenReturn(getProfileData());
		when(blogRepository.findByProfileId(Mockito.anyInt())).thenReturn(getBlogList());
		// WHEN

		BlogResponse response = new BlogResponse();
		Profile actualProfile = profileRepository.findByProfileId(userId);
		assertEquals(getProfileData(), actualProfile);

		response.setProfileId(actualProfile.getProfileId());
		response.setUserName(actualProfile.getUserName());

		List<Blog> blogList = blogRepository.findByProfileId(actualProfile.getProfileId());

		List<BlogListResponse> blogResponseList = new ArrayList<>();

		assertFalse(blogList.isEmpty());
		blogList.forEach(blog -> {

			BlogListResponse blogResponse = new BlogListResponse();
			blogResponse.setBlogId(blog.getBlogId());
			blogResponse.setBlogName(blog.getBlogName());
			blogResponse.setBlogDescirption(blog.getBlogDesc());
			blogResponse.setCreatedDate(StringHelper.getStringDateFromTimestamp(blog.getCreatedDate()));
			blogResponse.setUpdatedDate(StringHelper.getStringDateFromTimestamp(blog.getUpdatedDate()));
			blogResponseList.add(blogResponse);

		});

		response.setBlogResponse(blogResponseList);
		// THEN
		verify(profileRepository, times(1)).findByProfileId(Mockito.anyInt());
		verify(blogRepository, times(1)).findByProfileId(Mockito.anyInt());

	}

	@ParameterizedTest
	@MethodSource("getDummyArticleRequest")
	public void testAddArticle_sucsess(ArticleRequest request) {

		when(blogRepository.findByBlogId(Mockito.anyInt())).thenReturn(getBlogObject());
		when(articleRepository.save(Mockito.any(Article.class))).thenReturn(getArticleObject());
		// WHEN

		Blog actualBlog = blogRepository.findByBlogId(request.getBlogId());
		assertEquals(getBlogObject(), actualBlog);
		Article post = new Article();
		prepareArticleData(post, request, actualBlog);
		post = articleRepository.save(post);
		assertEquals(getArticleObject(), post);

		// THEN
		verify(blogRepository, times(1)).findByBlogId(Mockito.anyInt());
		verify(articleRepository, times(1)).save(Mockito.any(Article.class));

	}

	@ParameterizedTest
	@MethodSource("getDummyArticleRequest")
	public void testAddArticle_IfBlogNotFound(ArticleRequest request) {

		when(blogRepository.findByBlogId(Mockito.anyInt())).thenReturn(null);
		when(articleRepository.save(Mockito.any(Article.class))).thenReturn(getArticleObject());
		// WHEN

		Blog actualBlog = blogRepository.findByBlogId(request.getBlogId());
		assertNull(actualBlog, "blog not found");

		// THEN
		verify(blogRepository, times(1)).findByBlogId(Mockito.anyInt());
		verify(articleRepository, never()).save(Mockito.any(Article.class));

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

	@ParameterizedTest
	@MethodSource("getDummyArticleRequest")
	public void testUpdateArticle_sucsess(ArticleRequest request) {

		// GIVEN
		when(articleRepository.findByArticleId(Mockito.anyInt())).thenReturn(getArticleObject());
		when(blogRepository.findByBlogId(Mockito.anyInt())).thenReturn(getBlogObject());
		when(articleRepository.save(Mockito.any(Article.class))).thenReturn(getArticleObject());

		// WHEN

		Article post = articleRepository.findByArticleId(request.getArticleId());
		assertEquals(getArticleObject(), post);
		Blog blog = blogRepository.findByBlogId(post.getBlogId());
		assertEquals(getBlogObject(), blog);
		prepareArticleData(post, request, blog);
		articleRepository.save(post);

		// THEN
		verify(articleRepository, times(1)).findByArticleId(Mockito.anyInt());
		verify(blogRepository, times(1)).findByBlogId(Mockito.anyInt());
		verify(articleRepository, times(1)).save(Mockito.any(Article.class));

	}

	@ParameterizedTest
	@ValueSource(ints = { 1 })
	public void testDeleteArticle_sucsess(Integer articleId) {

		// GIVEN
		when(articleRepository.findByArticleId(Mockito.anyInt())).thenReturn(getArticleObject());

		// WHEN
		Article actaulArticle = articleRepository.findByArticleId(articleId);
		assertEquals(getArticleObject(), actaulArticle);
		articleRepository.delete(actaulArticle);

		// THEN
		verify(articleRepository, times(1)).findByArticleId(Mockito.anyInt());
		verify(articleRepository, times(1)).delete(Mockito.any(Article.class));

	}

	@ParameterizedTest
	@ValueSource(ints = { 1 })
	public void testGetArticle_sucsess(Integer blogId) {

		when(blogRepository.findByBlogId(Mockito.anyInt())).thenReturn(getBlogObject());
		when(articleRepository.findByBlogId(Mockito.anyInt())).thenReturn(getArticleList());

		ArticleResponse response = new ArticleResponse();
		Blog blog = blogRepository.findByBlogId(blogId);

		assertEquals(getBlogObject(), blog);

		response.setBlogId(blog.getBlogId());
		response.setBlogDescirption(blog.getBlogDesc());
		response.setBlogName(blog.getBlogName());

		List<Article> articleList = articleRepository.findByBlogId(blog.getBlogId());

		List<ArticleListResponse> articleResponseList = new ArrayList<>();
		assertFalse(articleList.isEmpty());
		articleList.forEach(article -> {

			ArticleListResponse articleResponse = new ArticleListResponse();
			articleResponse.setArticleId(article.getArticleId());
			articleResponse.setArticleText(article.getArticleText());
			articleResponse.setArticleTitle(article.getArticleTitle());
			articleResponse.setCreatedDate(StringHelper.getStringDateFromTimestamp(blog.getCreatedDate()));
			articleResponse.setUpdatedDate(StringHelper.getStringDateFromTimestamp(blog.getUpdatedDate()));

			articleResponseList.add(articleResponse);

		});

		response.setArticles(articleResponseList);

		// THEN

		verify(blogRepository, times(1)).findByBlogId(Mockito.anyInt());
		verify(articleRepository, times(1)).findByBlogId(Mockito.anyInt());

	}

}
