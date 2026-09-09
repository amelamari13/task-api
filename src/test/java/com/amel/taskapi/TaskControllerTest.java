package com.amel.taskapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ===== GET =====

    @Test
    void getTasksReturns200() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void getTaskWithExistingIdReturns200() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        MvcResult result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        Long id = node.get("id").asLong();

        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean the data"));
    }

    @Test
    void getTaskWithUnknownIdReturns404() throws Exception {
        mockMvc.perform(get("/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    // ===== POST =====

    @Test
    void postTaskCreatesTask() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Clean the data"))
                .andExpect(jsonPath("$.description").value("Create transform.py"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void postTaskWithEmptyTitleReturns400() throws Exception {
        String json = """
            {
                "title": "",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postTaskWithMissingStatusReturns400() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postTaskWithValidStoryPointsReturns201() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO",
                "storyPoints": 5
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storyPoints").value(5));
    }

    @Test
    void postTaskWithMissingStoryPointsReturns201() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.storyPoints").doesNotExist());
    }

    @Test
    void postTaskWithInvalidStoryPointsReturns400() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO",
                "storyPoints": 4
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    // ===== PUT =====

    @Test
    void putTaskWithExistingIdReturns200() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        String jsonUpdate = """
            {
                "title": "Configuration of Docker",
                "description": "Create the Dockerfile",
                "status": "IN_PROGRESS"
            }
            """;

        MvcResult result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        Long id = node.get("id").asLong();

        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Configuration of Docker"));
    }

    @Test
    void putTaskWithUnknownIdReturns404() throws Exception {
        String jsonUpdate = """
            {
                "title": "Configuration of Docker",
                "description": "Create the Dockerfile",
                "status": "IN_PROGRESS"
            }
            """;

        mockMvc.perform(put("/tasks/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isNotFound());
    }

    @Test
    void putTaskWithEmptyTitleReturns400() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        MvcResult result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        Long id = node.get("id").asLong();

        String jsonUpdate = """
            {
                "title": "",
                "description": "Create the Dockerfile",
                "status": "IN_PROGRESS"
            }
            """;

        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putTaskWithMissingStatusReturns400() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        MvcResult result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        Long id = node.get("id").asLong();

        String jsonUpdate = """
            {
                "title": "Configuration of Docker",
                "description": "Create the Dockerfile"
            }
            """;

        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isBadRequest());
    }

    // ===== DELETE =====

    @Test
    void deleteTaskWithExistingIdReturns200() throws Exception {
        String json = """
            {
                "title": "Clean the data",
                "description": "Create transform.py",
                "status": "TODO"
            }
            """;

        MvcResult result = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(responseBody);
        Long id = node.get("id").asLong();

        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTaskWithUnknownIdReturns404() throws Exception {
        mockMvc.perform(delete("/tasks/999999"))
                .andExpect(status().isNotFound());
    }
}