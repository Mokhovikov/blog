package com.example.fitness.Repository;


import com.example.fitness.Entity.Blog;
import com.example.fitness.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);


    Iterable<User> findByRolesName(String id);

    @Query(value = "select * from _user s where s.lastname like %:keyword% or s.email like %:keyword%", nativeQuery = true)
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT SUM (visit) FROM User")
    int getTotalViews();

    @Modifying
    @Transactional
    @Query("delete from User where email = :UserEmail")
    void deleteUsersByUserEmail(@Param("UserEmail") String UserEmail);

}
