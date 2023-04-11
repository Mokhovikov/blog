package com.example.fitness.Controller;

import com.example.fitness.Entity.Analyze;
import com.example.fitness.Entity.Blog;
import com.example.fitness.Entity.User;
import com.example.fitness.Repository.AnalyzeRepository;
import com.example.fitness.Repository.BlogRepository;
import com.example.fitness.Repository.UserRepository;
import com.example.fitness.UserService.BlogService;
import com.example.fitness.UserService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    private final BlogRepository blogRepository;

    private final AnalyzeRepository analyzeRepository;

    private final BlogService blogService;


    @RequestMapping(path ={"/users", "/search"})
    public String tasks(Model model, String keyword) {


        if (userRepository.findAll().isEmpty()) {

            return "redirect:/";
        }if (keyword != null) {
            List<User> users = userService.getByKeyword(keyword);
            model.addAttribute("users", users);
            return "admin/users";
        }
        else if (!userRepository.findAll().isEmpty()) {

            Iterable<User> users = userRepository.findByRolesName("USER");
            model.addAttribute("users", users);
            return "admin/users";
        }

        return null;
    }


    @PostMapping("/deleteUser")
    public String deleteUser(Model model, @RequestParam String UserEmail) {
        userRepository.deleteById(UserEmail);
        return "redirect:/admin/users";
    }

    @PostMapping("/blockUser")
    public String blockUser(Model model, @RequestParam String UserEmail) {
        User user = userRepository.getUserByEmail(UserEmail);
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/unblockUser")
    public String unblockUser(Model model, @RequestParam String UserEmail) {
        User user = userRepository.getUserByEmail(UserEmail);
        user.setEnabled(false);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/personal")
    public String personal(Model model, Authentication authentication, String email) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);
        model.addAttribute("user", user);
        return "admin/admin_personal";
    }

    @PostMapping("/q")
    public String loginMember(
            @RequestParam String email

    ) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        System.out.println("Personal " + email);
        return "redirect:/admin/personal";
    }

    @PostMapping("/update")
    public String update(Model model, Authentication authentication, String email,
                         @RequestParam String phone,
                         @RequestParam String firstname,
                         @RequestParam String lastname
    ) throws IOException {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        User user = userService.getMemberByEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setNumber(phone);

        userService.update(user);

        model.addAttribute("user", user);
        return "redirect:/admin/personal";
    }

    @GetMapping("/add")
    public String add() {
        return "admin/add_articles";
    }

    @PostMapping("/add")
    public String saveArticle(@RequestParam String title,
                              @RequestParam String text,
                              @RequestParam String description) {

        Blog blog = new Blog();

        blog.setTitle(title);
        blog.setDescription(description);
        blog.setText(text);

        blogRepository.save(blog);

        return "redirect:/admin/personal";
    }


    @GetMapping("/stat")
    public String stat(Model model){

        List<Analyze> analyzeIterable = analyzeRepository.findAll();
        model.addAttribute("stats", analyzeIterable);



        List<Blog> blogIterable = blogRepository.findAll();
        model.addAttribute("blogs", blogIterable);


        HashMap<String, Double> averageAge = new HashMap<>();
        blogIterable.forEach(blog -> averageAge.put(blog.getTitle(),analyzeRepository.getAverageAgeById(blog.getId())));
        model.addAttribute("AverageAge", averageAge);

        HashMap<String, Integer> menAnalyze = new HashMap<>();
        blogIterable.forEach(blog -> menAnalyze.put(blog.getTitle(), analyzeRepository.getAllBySexAndIdArticle("Man", blog.getId()).size()));
        model.addAttribute("amountMan", menAnalyze);

        HashMap<String, Integer> womenAnalyze = new HashMap<>();
        blogIterable.forEach(blog -> womenAnalyze.put(blog.getTitle(), analyzeRepository.getAllBySexAndIdArticle("Women", blog.getId()).size()));
        model.addAttribute("amountWomen", womenAnalyze);

        return "admin/stat";
    }

    @RequestMapping(path ={"/allblog", "/searchBlog"})
    public String allblogs(Model model, String keyword) {


        if (blogRepository.findAll().isEmpty()) {

            return "redirect:/";
        }if (keyword != null) {
            List<Blog> blogs = blogService.getByKeyword(keyword);
            model.addAttribute("blogs", blogs);
            return "admin/all_articles";
        }
        else if (!blogRepository.findAll().isEmpty()) {

            Iterable<Blog> blogs = blogRepository.findAll();
            model.addAttribute("blogs", blogs);
            return "admin/all_articles";
        }

        return null;
    }

    @PostMapping("/deleteBlog")
    public String deleteBlog(Model model, @RequestParam int id) {
        Blog blog = blogRepository.findById(id);
        blogRepository.delete(blog);

        if(analyzeRepository.existsById(id)){
            Analyze analyze = analyzeRepository.findAnalyzeByIdArticle(id);
            analyzeRepository.delete(analyze);
        }

        return "redirect:/admin/allblog";
    }


    @GetMapping("/editBlog/{id}")
    public String editBlog(@PathVariable(value = "id") int id, Model model) {
        Blog blog = blogRepository.findById(id);
        model.addAttribute("blog", blog);

        return "admin/editArticle";
    }

    @PostMapping("/editBlog")
    public String saveEditBlog(@RequestParam int id, Model model,
                               @RequestParam String title, @RequestParam String description,
                               @RequestParam String text) {
        Blog blog = blogRepository.findById(id);
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setText(text);
        blogRepository.save(blog);
        System.out.println("Edited");

        return "redirect:/admin/allblog";
    }
}
