package com.example.blog;


import com.example.blog.Entity.User;
import com.example.blog.OAuth2.CustomOAuth2User;
import com.example.blog.OAuth2.CustomOAuth2UserService;
import com.example.blog.UserService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig extends WebSecurityConfigurerAdapter {


    private final CustomOAuth2UserService oauthUserService;

    private final UserServiceImpl userServiceImpl;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/signUp", "/", "/search").permitAll()
                .antMatchers("/user/**", "/blog/{id}", "/blog").hasAuthority("USER")
                .antMatchers("/user/**","/blog", "/blog/{id}").authenticated()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                //.anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .loginPage("/login")
                    .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                           .successHandler((request, response, authentication) -> {
                                System.out.println("AuthenticationSuccessHandler invoked");
                                System.out.println("Authentication name: " + authentication.getName());
                                User user = userServiceImpl.getMemberByEmail(authentication.getName());
                                System.out.println(authentication.getAuthorities());

                                if (!user.isEnabled()) {
                                    if (user.getRoles().toString().equals("[ADMIN]")) {
                                        response.sendRedirect("/admin/personal");
                                    } else if (user.getRoles().toString().equals("[USER]")) {
                                        response.sendRedirect("/user/personal");
                                    }
                                }else {
                                    response.sendRedirect("/block");
                                }

                            })
                .and()
                .oauth2Login()
                .loginPage("/login")
                    .userInfoEndpoint()
                        .userService(oauthUserService)
                        .and()
                             .successHandler((request, response, authentication) -> {
                                System.out.println("AuthenticationSuccessHandler invoked");
                                System.out.println("Authentication name: " + authentication.getName());

                                    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

                                    userServiceImpl.processOAuthPostLogin(oauthUser.getEmail(), oauthUser.getAttributes().get("given_name").toString(),
                                            oauthUser.getAttributes().get("family_name").toString());

                                    User user = userServiceImpl.getMemberByEmail(oauthUser.getEmail());

                                    System.out.println(user.getRoles());
                                    System.out.println(oauthUser.getAttributes());

                                    if (!user.isEnabled()) {

                                        response.sendRedirect("/user/personal");
                                    } else {
                                        response.sendRedirect("/block");
                                    }
                             })

                //.defaultSuccessUrl("/list")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .logout().logoutUrl("/logout")
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }


    private final DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select email as principal, password as credentails, true from _user where email=?")
                .authoritiesByUsernameQuery("select _user_email as principal, role_name as role from _user_roles where _user_email=?")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // TODO Auto-generated method stub
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(

                // статика
                "/css/**",
                "/js/**",
                "/fonts/**",
                "/images/**",
                "/playground_assets/**",
                "/static/**"
        );
    }

}
