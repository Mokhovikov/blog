package com.example.fitness.Repository;

import com.example.fitness.Entity.Analyze;
import com.example.fitness.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnalyzeRepository extends JpaRepository<Analyze, Integer> {

    boolean existsAnalyzeByUserEmailAndIdArticle(String email, int id);

    Analyze findAnalyzeByUserEmailAndIdArticle(String email, int id);

    Analyze findAnalyzeByIdArticle(int id);

    @Query("SELECT AVG(age) AS Average_age FROM Analyze u WHERE u.idArticle = :idArticle")
    double getAverageAgeById(@Param("idArticle") int idArticle);

/*    @Query("SELECT u FROM Analyze u WHERE u.sex = :sex  u.idArticle = :idarticle")
    Analyze getTitleSexAndIdArticle(@Param("sex") String sex, @Param("idArticle") int idArticle);*/

    List<Analyze> getAllBySexAndIdArticle(String s, int id);


    void deleteAnalyzeByIdArticle(int id);

}
