package com.example.blog;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value="rob", username = "admintest@gmail.com", password = "12345678", roles="ADMIN")
public @interface WithMockAdmin {
}
