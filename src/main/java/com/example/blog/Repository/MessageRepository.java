package com.example.blog.Repository;

import com.example.blog.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "select * from message s where s.name like %:keyword% or s.email like %:keyword%", nativeQuery = true)
    List<Message> findByKeyword(@Param("keyword") String keyword);

}
