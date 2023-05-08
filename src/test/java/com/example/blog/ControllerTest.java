package com.example.blog;

import com.example.blog.Entity.User;
import com.example.blog.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor

public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @Test
    void getGreeting() throws Exception {
        mockMvc
                .perform(get("/"))
                // выводим информацию о запросе и ответе в консоль (можно не выводить, это сделано для красоты)
                .andDo(print())
                // ожидаем HTTP код 200
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})
    void getBlog() throws Exception {
        mockMvc
                .perform(get("/blog"))
                // выводим информацию о запросе и ответе в консоль (можно не выводить, это сделано для красоты)
                .andDo(print())
                // ожидаем HTTP код 200
                .andExpect(status().isOk());
    }

    @Test
    void getLogin() throws Exception {
        mockMvc
                .perform(get("/login"))
                // выводим информацию о запросе и ответе в консоль (можно не выводить, это сделано для красоты)
                .andDo(print())
                // ожидаем HTTP код 200
                .andExpect(status().isOk());
    }

    @Test
    void getSignUp() throws Exception {
        mockMvc
                .perform(get("/signUp"))
                // выводим информацию о запросе и ответе в консоль (можно не выводить, это сделано для красоты)
                .andDo(print())
                // ожидаем HTTP код 200
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})
    void getBlogId() throws Exception {
        mockMvc
                .perform(get("/blog/{id}",1))
                // выводим информацию о запросе и ответе в консоль (можно не выводить, это сделано для красоты)
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})
    void getPersonal() throws Exception {
        mockMvc
                .perform(get("/user/personal"))
                // выводим информацию о запросе и ответе в консоль (можно не выводить, это сделано для красоты)
                .andExpect(status().isOk()).andReturn();
    }

    @Test
   /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getLoginPost() throws Exception {


        mockMvc.perform(post("/login")
                        .param("username","iimohovikov@gmail.com")
                        .param("password", "12345678")
                        .with(csrf()))
                        .andExpect(status().is3xxRedirection());
    }

    @Test
        /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getSignUpPost() throws Exception {


        mockMvc.perform(post("/login")
                        .param("firstname","Ivan")
                        .param("lastname","Mokhovikov")
                        .param("age","18")
                        .param("phone","+375445395242")
                        .param("sex","Man")
                        .param("username","iimohovikov@gmail.com")
                        .param("password", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getMessage() throws Exception {


        mockMvc.perform(post("/sendMessage")
                        .param("name","Ivan")
                        .param("email","Mokhovikov")
                        .param("message","18")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @Test
        /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getUp() throws Exception {


        mockMvc.perform(post("/user/update")
                        .param("firstname","Ivan")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
        /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getUserSearch() throws Exception {


        mockMvc.perform(post("/admin/search")
                        .param("keyword","Ivan")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
        /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getDeleteUser() throws Exception {


        mockMvc.perform(post("/admin/DeleteUser")
                        .param("userEmail","a@a.a")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
        /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getSearch() throws Exception {


        mockMvc.perform(post("/admin/searchStat")
                        .param("keyword","AI")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
        /* @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})*/
    void getLogout() throws Exception {


        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username="iimohovikov@gmail.com",password = "12345678",roles={"USER"})
    void getDeniedAccess() throws Exception {


        mockMvc.perform(get("/admin/Personal").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
