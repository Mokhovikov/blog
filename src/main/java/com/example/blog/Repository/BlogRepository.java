package com.example.blog.Repository;

import com.example.blog.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

    Blog findById(int id);


    //Custom query
    @Query(value = "select * from blog_articles s where s.title like %:keyword% or s.description like %:keyword%", nativeQuery = true)
    List<Blog> findByKeyword(@Param("keyword") String keyword);

}
