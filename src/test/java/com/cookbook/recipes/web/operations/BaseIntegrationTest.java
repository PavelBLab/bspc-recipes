package com.cookbook.recipes.web.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Header;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RecipesOperation.class)
abstract public class BaseIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    Header getCustomerHeader(final String recipeId) {
        return new Header(HttpHeaders.AUTHORIZATION, "Bearer customer");
    }

}
