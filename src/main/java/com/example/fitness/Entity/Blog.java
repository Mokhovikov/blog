package com.example.fitness.Entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@Table(name = "blog_articles")
@Getter
@Setter
public class Blog {

    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String text;

    @Column
    private String img;

    @Column
    private int views;

}
