package com.example.fitness.Controller;


import com.example.fitness.Entity.*;
import com.example.fitness.OAuth2.CustomOAuth2User;
import com.example.fitness.Repository.*;
import com.example.fitness.UserService.BlogService;
import com.example.fitness.UserService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {

    private final UserServiceImpl userService;

    private final BlogService blogService;
    private final UserRepository userRepository;

    private final BlogRepository blogRepository;

    private final AnalyzeRepository analyzeRepository;

    private final ProjectRepository projectRepository;

    private final MessageRepository messageRepository;

    @GetMapping("/")
    public String start(Model model) {
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);

        return "index";
    }


    @GetMapping("/signUp")
    public String signUp() {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUpPost(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String password) {

        User user = new User(firstname, lastname, email, password);
        userService.createUser(user);


        return "redirect:/login";
    }

    @RequestMapping(path = {"/blog", "/search"})
    public String blog(Model model, String keyword) {

        if (blogRepository.findAll().isEmpty()) {

            return "redirect:/";
        }
        if (keyword != null) {
            List<Blog> blogs = blogService.getByKeyword(keyword);
            model.addAttribute("blogs", blogs);
            return "blog";
        } else if (!blogRepository.findAll().isEmpty()) {

            Iterable<Blog> blogs = blogRepository.findAll();
            model.addAttribute("blogs", blogs);
            return "blog/blog";
        }
        return null;
    }


    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") int id, Model model) {
        if (!blogRepository.existsById(id)) {
            return "redirect:/blog";
        }else if (blogRepository.existsById(id)) {

       /* Optional<Blog> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);*/
            Blog blog = blogRepository.findById(id);
            int views = blog.getViews();
            views++;
            blog.setViews(views);
            blogRepository.save(blog);

            org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User user = userService.getMemberByEmail(email);

            if (!analyzeRepository.existsAnalyzeByUserEmailAndIdArticle(user.getEmail(), id)) {
                Analyze analyze = new Analyze();
                analyze.setFirstname(user.getFirstname());
                analyze.setLastname(user.getLastname());
                analyze.setUserEmail(user.getEmail());
                analyze.setAge(user.getAge());
                analyze.setSex(user.getSex());
                analyze.setIdArticle(id);
                analyze.setView(0);
                analyzeRepository.save(analyze);
            } else if (analyzeRepository.existsAnalyzeByUserEmailAndIdArticle(user.getEmail(), id)) {
                Analyze analyze = analyzeRepository.findAnalyzeByUserEmailAndIdArticle(user.getEmail(), id);

                int view = analyze.getView();
                view++;
                analyze.setView(view);
                analyzeRepository.save(analyze);
            }
            /*System.out.println("Просмотры " + blog.getViews());*/
            model.addAttribute("blog", blog);


            return "blog/articles";
        }
        return null;
    }


    @PostMapping("/loading")
    public String loginMember(
            @RequestParam String email

    ){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        System.out.println(auth.getAuthorities());

        System.out.println("Personal " + email);
        User user = userRepository.getUserByEmail(email);
        if(!user.isEnabled()){
            return "redirect:/personal";
        }else {
            return "redirect:/block";
        }

    }


    @GetMapping("/block")
    public String block() {
        return "block";
    }


/*    @GetMapping("/user/**")
    public String user(String email) {

        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        System.out.println(auth.getAuthorities());

        System.out.println("Personal " + email);
        User user = userRepository.getUserByEmail(email);
        CustomOAuth2User oauthUser = (CustomOAuth2User) auth.getPrincipal();
        User user1 = userService.getMemberByEmail(oauthUser.getEmail());

        if (user.getRoles().toString().equals("[ADMIN]")) {
            return "403";
        } else {
            return "redirect:/user/personal";
        }

    }*/

    @GetMapping("/personal")
    public String personal(Model model){
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userService.getMemberByEmail(email);

        if(user.getRoles().toString().equals("[ADMIN]")){
            model.addAttribute("user", user);
            return "redirect:/admin/personal";
        } else if (user.getRoles().toString().equals("[USER]")) {
            model.addAttribute("user", user);
            return "redirect:/user/personal";
        }
        return null;
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String message){
        Message message1 = new Message();
        message1.setMessage(message);
        message1.setName(name);
        message1.setEmail(email);

        messageRepository.save(message1);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String retur(){
        return "redirect:/";
    }

}

