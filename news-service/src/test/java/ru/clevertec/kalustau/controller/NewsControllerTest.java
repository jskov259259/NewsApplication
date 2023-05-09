package ru.clevertec.kalustau.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.kalustau.service.NewsService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.Constants.TEST_TOKEN;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoRequest;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoResponse;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class NewsControllerTest {

    @InjectMocks
    private NewsController newsController;

    @Mock
    private NewsService newsService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(newsController)
                .build();
    }

    @AfterEach
    public void end() {
        Mockito.verifyNoMoreInteractions(newsService);
    }

    @Test
    void checkFindAll() throws Exception {
        doReturn(Collections.singletonList(getNewsDtoResponse()))
                .when(newsService).findAll(any(), anyInt(), anyInt(), anyString());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("search", TEST_SEARCH);
        requestParams.add("pageNo", String.valueOf(TEST_PAGE_NO));
        requestParams.add("pageSize", String.valueOf(TEST_PAGE_SIZE));
        requestParams.add("sortBy", TEST_SORT_BY);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news")
                .params(requestParams)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("Text1")));

        Mockito.verify(newsService).findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
    }

    @Test
    void checkFindById() throws Exception {
        doReturn(getNewsDtoResponse())
                .when(newsService).findById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        Mockito.verify(newsService).findById(TEST_ID);
    }

    @Test
    void checkCreate() throws Exception {
        doReturn(getNewsDtoResponse())
                .when(newsService).save(any(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/news")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", TEST_TOKEN)
                .content(new ObjectMapper().writeValueAsString(getNewsDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        Mockito.verify(newsService).save(getNewsDtoRequest(), TEST_TOKEN);
    }

    @Test
    void checkUpdate() throws Exception {
        doReturn(getNewsDtoResponse())
                .when(newsService).update(anyLong(), any(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/news/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", TEST_TOKEN)
                .content(new ObjectMapper().writeValueAsString(getNewsDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        Mockito.verify(newsService).update(TEST_ID, getNewsDtoRequest(), TEST_TOKEN);
    }

    @Test
    void checkDelete() throws Exception {
        doNothing().when(newsService).deleteById(anyLong(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/news/1")
                .header("Authorization", TEST_TOKEN)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(newsService).deleteById(TEST_ID, TEST_TOKEN);
    }

}