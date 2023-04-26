package com.example.fitness.Repository;

import com.example.fitness.Entity.Analyze;
import com.example.fitness.Entity.Blog;
import com.example.fitness.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnalyzeRepository extends JpaRepository<Analyze, Integer> {

    boolean existsAnalyzeByUserEmailAndIdArticle(String email, int id);

    Analyze findAnalyzeByUserEmailAndIdArticle(String email, int id);

    Analyze findAnalyzeByIdArticle(int id);

    @Query("SELECT AVG(age) AS Average_age FROM Analyze u WHERE u.idArticle = :idArticle")
    double getAverageAgeById(@Param("idArticle") int idArticle);


    List<Analyze> getAllBySexAndIdArticle(String s, int id);

    @Query("SELECT SUM (u.view) AS views FROM Analyze u WHERE u.idArticle = :idArticle and u.sex = :sex")
    int getViewsByIdArticleAndSex(@Param("idArticle") int idArticle,@Param("sex") String sex);

    @Query("SELECT SUM (u.view) AS views FROM Analyze u WHERE u.idArticle = :idArticle")
    int getViewsByIdArticle(@Param("idArticle") int idArticle);

    @Query("SELECT SUM (view) AS views FROM Analyze")
    int getTotalViews();

    @Query(value = "select * from  analyze_blog s where s.title like %:keyword%", nativeQuery = true)
    List<Analyze> findByKeyword(@Param("keyword") String keyword);

    void deleteAnalyzeByIdArticle(int id);

  /*  @Query("DELETE FROM Analyze s WHERE  s.userEmail =:userEmail")*/
    @Transactional
    void deleteByUserEmail(String userEmail);

}
