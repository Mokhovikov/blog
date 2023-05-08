package com.example.blog.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@Table(name = "analyze_blog")
@Getter
@Setter
public class Analyze {

    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String userEmail;

    @Column
    private String sex;

    @Column
    private int age;

    @Column
    private int view;

    @Column
    private  int idArticle;
}
