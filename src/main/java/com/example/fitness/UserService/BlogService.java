package com.example.fitness.UserService;

import com.example.fitness.Entity.Blog;
import com.example.fitness.Repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    /*
     * TODO: Get Shop By keyword
     */
    public List<Blog> getByKeyword(String keyword) {
        return blogRepository.findByKeyword(keyword);
    }

}
