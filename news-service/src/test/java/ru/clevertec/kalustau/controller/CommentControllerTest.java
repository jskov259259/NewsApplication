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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import ru.clevertec.kalustau.dto.CommentDtoRequest;
import ru.clevertec.kalustau.dto.Proto;
import ru.clevertec.kalustau.mapper.CommentMapper;
import ru.clevertec.kalustau.model.Comment;
import ru.clevertec.kalustau.service.CommentService;

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
import static ru.clevertec.kalustau.util.TestData.getComment;
import static ru.clevertec.kalustau.util.TestData.getCommentDtoRequest;
import static ru.clevertec.kalustau.util.TestData.getCommentDtoResponse;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Mock
    private CommentMapper commentMapper;

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
        Comment comment = getComment();
        Proto.CommentDtoResponse commentDto = getCommentDtoResponse();
        doReturn(Collections.singletonList(comment))
                .when(commentService).findAll(any(), anyInt(), anyInt(), anyString());
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

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

        verify(commentService).findAll(TEST_SEARCH, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        verify(commentMapper).commentToDto(any());
    }

    @Test
    void checkFindById() throws Exception {
        Comment comment = getComment();
        Proto.CommentDtoResponse commentDto = getCommentDtoResponse();
        doReturn(comment)
                .when(commentService).findById(anyLong());
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/comments/1")
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        verify(commentService).findById(TEST_ID);
        verify(commentMapper).commentToDto(any());
    }

    @Test
    void checkFindAllCommentsByNewsId() throws Exception {
        Comment comment = getComment();
        Proto.CommentDtoResponse commentDto = getCommentDtoResponse();
        doReturn(Collections.singletonList(comment))
                .when(commentService).findAllByNewsId(anyLong(), anyInt(), anyInt(), anyString());
        doReturn(commentDto)
                .when(commentMapper).commentToDto(comment);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("pageNo", String.valueOf(TEST_PAGE_NO));
        requestParams.add("pageSize", String.valueOf(TEST_PAGE_SIZE));
        requestParams.add("sortBy", TEST_SORT_BY);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/1/comments")
                .params(requestParams)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("User1")));

        verify(commentService).findAllByNewsId(TEST_ID, TEST_PAGE_NO, TEST_PAGE_SIZE, TEST_SORT_BY);
        verify(commentMapper).commentToDto(any());
    }

    @Test
    void checkCreate() throws Exception {
        Comment comment = getComment();
        CommentDtoRequest commentDtoRequest = getCommentDtoRequest();
        Proto.CommentDtoResponse commentDtoResponse = getCommentDtoResponse();
        doReturn(comment)
                .when(commentService).save(anyLong(), any(), anyString());
        doReturn(comment)
                .when(commentMapper).dtoToComment(commentDtoRequest);
        doReturn(commentDtoResponse)
                .when(commentMapper).commentToDto(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/news/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
                .content(new ObjectMapper().writeValueAsString(getCommentDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        verify(commentService).save(TEST_ID, comment, TEST_TOKEN);
        verify(commentMapper).dtoToComment(commentDtoRequest);
        verify(commentMapper).commentToDto(comment);
    }

    @Test
    void checkUpdate() throws Exception {
        Comment comment = getComment();
        CommentDtoRequest commentDtoRequest = getCommentDtoRequest();
        Proto.CommentDtoResponse commentDtoResponse = getCommentDtoResponse();
        doReturn(comment)
                .when(commentService).update(anyLong(), any(), anyString());
        doReturn(comment)
                .when(commentMapper).dtoToComment(commentDtoRequest);
        doReturn(commentDtoResponse)
                .when(commentMapper).commentToDto(comment);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
                .content(new ObjectMapper().writeValueAsString(getCommentDtoRequest()))
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("text", Matchers.is("Text1")))
                .andExpect(MockMvcResultMatchers.jsonPath("userName", Matchers.is("User1")));

        verify(commentService).update(TEST_ID, comment, TEST_TOKEN);
        verify(commentMapper).dtoToComment(commentDtoRequest);
        verify(commentMapper).commentToDto(comment);
    }

    @Test
    void checkDelete() throws Exception {
        doNothing().when(commentService).deleteById(anyLong(), anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/comments/1")
                .header(HttpHeaders.AUTHORIZATION, TEST_TOKEN)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService).deleteById(TEST_ID, TEST_TOKEN);
    }

}