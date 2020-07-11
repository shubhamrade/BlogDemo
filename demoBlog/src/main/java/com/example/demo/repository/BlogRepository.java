package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

	List<Blog> findByProfileId(Integer userId);

	Blog findByProfileIdAndBlogNameIgnoreCase(Integer profileId, String blogName);

	Blog findByBlogId(Integer blogId);

}
