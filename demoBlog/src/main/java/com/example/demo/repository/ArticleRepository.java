package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.demo.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Modifying
	void deleteByBlogId(Integer blogId);

	List<Article> findByBlogId(Integer blogId);

	Article findByArticleId(Integer articleId);

}
