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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.kalustau.dto.NewsDtoRequest;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.mapper.NewsMapper;
import ru.clevertec.kalustau.model.News;
import ru.clevertec.kalustau.service.NewsService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static ru.clevertec.kalustau.util.Constants.TEST_ID;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_NO;
import static ru.clevertec.kalustau.util.Constants.TEST_PAGE_SIZE;
import static ru.clevertec.kalustau.util.Constants.TEST_SEARCH;
import static ru.clevertec.kalustau.util.Constants.TEST_SORT_BY;
import static ru.clevertec.kalustau.util.Constants.TEST_TOKEN;
import static ru.clevertec.kalustau.util.TestData.getNews;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoRequest;
import static ru.clevertec.kalustau.util.TestData.getNewsDtoResponse;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class NewsControllerTest {

    @InjectMocks
    private NewsController newsController;

    @Mock
    private NewsService newsService;

    @Mock
    private NewsMapper newsMapper;

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
        News news = getNews();
        Proto.NewsDtoResponse newsDto = getNewsDtoResponse();
        doReturn(Collections.singletonList(news))
                .when(newsService).findAll(any(), anyInt(), anyInt(), anyString());
        doReturn(newsDto)
                .when(newsMapper).newsToDto(news);

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

        verify(newsService).findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        verify(newsMapper).newsToDto(any());
    }

    @Test
    void checkFindById() throws Exception {
        News news = getNews();
        Proto.NewsDtoResponse newsDto = getNewsDtoResponse();
        doReturn(news)
                .when(newsService).findById(anyLong());
        doReturn(newsDto)
                .when(newsMapper).newsToDto(news);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        Mockito.verify(newsService).findById(TEST_ID);
        verify(newsMapper).newsToDto(any());
    }

    @Test
    void checkCreate() throws Exception {
        News news = getNews();
        NewsDtoRequest newsDtoRequest = getNewsDtoRequest();
        Proto.NewsDtoResponse newsDtoResponse = getNewsDtoResponse();
        doReturn(news)
                .when(newsService).save(any(), anyString());
        doReturn(news)
                .when(newsMapper).dtoToNews(newsDtoRequest);
        doReturn(newsDtoResponse)
                .when(newsMapper).newsToDto(news);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/news")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
                .content(new ObjectMapper().writeValueAsString(getNewsDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        verify(newsService).save(news, TEST_TOKEN);
        verify(newsMapper).dtoToNews(newsDtoRequest);
        verify(newsMapper).newsToDto(news);
    }

    @Test
    void checkUpdate() throws Exception {
        News news = getNews();
        NewsDtoRequest newsDtoRequest = getNewsDtoRequest();
        Proto.NewsDtoResponse newsDtoResponse = getNewsDtoResponse();
        doReturn(news)
                .when(newsService).update(anyLong(), any(), anyString());
        doReturn(news)
                .when(newsMapper).dtoToNews(newsDtoRequest);
        doReturn(newsDtoResponse)
                .when(newsMapper).newsToDto(news);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/news/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
                .content(new ObjectMapper().writeValueAsString(getNewsDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("title", Matchers.is("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")));

        verify(newsService).update(TEST_ID, news, TEST_TOKEN);
        verify(newsMapper).dtoToNews(newsDtoRequest);
        verify(newsMapper).newsToDto(news);
    }

    @Test
    void checkDelete() throws Exception {
        doNothing().when(newsService).deleteById(anyLong(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/news/1")
                .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(newsService).deleteById(TEST_ID, TEST_TOKEN);
    }

}