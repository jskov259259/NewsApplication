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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.kalustau.service.CommentService;

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
import static ru.clevertec.kalustau.util.TestData.getCommentDto;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .build();
    }

    @AfterEach
    public void end() {
        Mockito.verifyNoMoreInteractions(commentService);
    }

    @Test
    void checkFindAll() throws Exception {
        doReturn(Collections.singletonList(getCommentDto()))
                .when(commentService).findAll(any(), anyInt(), anyInt(), anyString());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("search", TEST_SEARCH);
        requestParams.add("pageNo", String.valueOf(TEST_PAGE_NO));
        requestParams.add("pageSize", String.valueOf(TEST_PAGE_SIZE));
        requestParams.add("sortBy", TEST_SORT_BY);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments")
                .params(requestParams)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("User1")));

        Mockito.verify(commentService).findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
    }

    @Test
    void checkFindById() throws Exception {
        doReturn(getCommentDto())
                .when(commentService).findById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        Mockito.verify(commentService).findById(TEST_ID);
    }

    @Test
    void checkFindAllCommentsByNewsId() throws Exception {
        doReturn(Collections.singletonList(getCommentDto()))
                .when(commentService).findAllByNewsId(anyLong(), anyInt(), anyInt(), anyString());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("pageNo", String.valueOf(TEST_PAGE_NO));
        requestParams.add("pageSize", String.valueOf(TEST_PAGE_SIZE));
        requestParams.add("sortBy", TEST_SORT_BY);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/1/comments")
                .params(requestParams)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("User1")));

        Mockito.verify(commentService).findAllByNewsId(TEST_ID, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
    }

    @Test
    void checkCreate() throws Exception {
        doReturn(getCommentDto())
                .when(commentService).save(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/news/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getCommentDto()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        Mockito.verify(commentService).save(TEST_ID, getCommentDto());
    }

    @Test
    void checkUpdate() throws Exception {
        doReturn(getCommentDto())
                .when(commentService).update(anyLong(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(getCommentDto()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        Mockito.verify(commentService).update(TEST_ID, getCommentDto());
    }

    @Test
    void checkDelete() throws Exception {
        doNothing().when(commentService).deleteById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/comments/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(commentService).deleteById(TEST_ID);
    }

}