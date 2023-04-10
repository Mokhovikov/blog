package com.example.fitness.Controller;


import com.example.fitness.Entity.User;
import com.example.fitness.OAuth2.CustomOAuth2User;
import com.example.fitness.UserService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;


    @PostMapping("/update")
    public String update(Model model, Authentication authentication, String email, HttpServletResponse response,
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
        return "redirect:/user/personal";
    }

    @GetMapping("/personal")
    public String personal(Model model, Authentication auth, String email) {

        auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();

     /*   CustomOAuth2User oauthUser = (CustomOAuth2User) auth.getPrincipal();
        User user = userService.getMemberByEmail(oauthUser.getEmail());*/
        User user = userService.getMemberByEmail(email);
        model.addAttribute("user", user);

        model.addAttribute("email", email);
        return "user/personal";
    }

    @GetMapping("/google/personal")
    public String googlePersonal(Authentication auth, Model model){
        CustomOAuth2User oauthUser = (CustomOAuth2User) auth.getPrincipal();
        User user = userService.getMemberByEmail(oauthUser.getEmail());
        System.out.println(user.getEmail());

        model.addAttribute("user", user);
        return "user/googlePersonal";
    }

/*    @PostMapping("/q")
    public String loginMember(
            @RequestParam String email

    ) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        email = auth.getName();
        System.out.println("Personal " + email);
        return "redirect:/user/personal";
    }*/


}
