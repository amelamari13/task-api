package com.amel.taskapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTasksReturns200() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void postTaskCreatesTask() throws Exception {
        String json = """
            {
                "title": "Nettoyer les données",
                "description": "Créer transform.py",
                "status": "TODO"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nettoyer les données"))
                .andExpect(jsonPath("$.description").value("Créer transform.py"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void postTaskWithEmptyTitleReturns400() throws Exception {
        String json = """
            {
                "title": "",
                "description": "Créer transform.py",
                "status": "TODO"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTaskWithUnknownIdReturns404() throws Exception {
        mockMvc.perform(get("/tasks/999999"))
                .andExpect(status().isNotFound());
    }
}