package com.idatt2105.backend.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idatt2105.backend.dto.QuizDTO;
import com.idatt2105.backend.dto.UserDTO;
import com.idatt2105.backend.model.Category;
import com.idatt2105.backend.model.Tag;
import com.idatt2105.backend.service.QuizService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** The QuizControllerTests class is a test class that tests the QuizController class. */
@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuizControllerTests {
  @Autowired MockMvc mockMvc;

  @MockBean private QuizService quizService;

  /**
   * The BasicFunctionalityTests class is a test class that tests the basic functionality of the
   * QuizController class.
   */
  @Nested
  class BasicFunctionalityTests {
    QuizDTO quizDTO;
    UserDTO userDTO;
    Set<UserDTO> users;
    Set<Tag> tags;

    @BeforeEach
    void setUp() {
      quizDTO = new QuizDTO();
      quizDTO.setId(1L);
      quizDTO.setTitle("Quiz Title");
      quizDTO.setDescription("Quiz Description");

      userDTO = new UserDTO();
      userDTO.setId(1L);
      userDTO.setUsername("TestUser");

      users = new HashSet<>();
      users.add(userDTO);

      Tag tag = new Tag();
      tag.setId(1L);
      tag.setTagName("TestTag");

      tags = new HashSet<>();
      tags.add(tag);

      when(quizService.save(any(QuizDTO.class))).thenReturn(quizDTO);
      when(quizService.getQuizById(1L)).thenReturn(quizDTO);
      when(quizService.getUsersByQuizId(1L)).thenReturn(users);
      when(quizService.addTags(any(QuizDTO.class))).thenReturn(quizDTO);
      when(quizService.deleteTags(any(QuizDTO.class))).thenReturn(quizDTO);

      List<QuizDTO> quizzes = new ArrayList<>();
      quizzes.add(quizDTO);
      Page<QuizDTO> quizPage = new PageImpl<>(quizzes);
      when(quizService.getAllQuizzes(any())).thenReturn(quizPage);
    }

    /**
     * This method tests the behavior of the getAllQuizzes endpoint.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the quizzes are
     * successfully retrieved.
     *
     * @throws Exception if the test fails
     */
    @Test
    void getAllQuizzesReturnsOkAndQuizzes() throws Exception {
      mockMvc.perform(get("/api/quizzes").secure(true)).andExpect(status().isOk());
    }

    /**
     * This method tests the behavior of the getQuizById endpoint with a valid quiz ID.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when a quiz with the
     * specified ID exists.
     *
     * @throws Exception if the test fails
     */
    @Test
    void getQuizByIdReturnsOkAndQuiz() throws Exception {
      mockMvc.perform(get("/api/quizzes/1").secure(true)).andExpect(status().isOk());
    }

    /**
     * This method tests the behavior of the createQuiz endpoint with a valid body.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 201 Created when the quiz is
     * successfully created.
     *
     * @throws Exception if the test fails
     */
    @Test
    void createQuizReturnsCreatedAndQuiz() throws Exception {
      mockMvc
          .perform(
              post("/api/quizzes")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(asJsonString(quizDTO))
                  .secure(true))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(1));
    }

    /**
     * This method tests the updateQuiz endpoint with a valid quiz ID and body.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the quiz is
     * successfully updated.
     *
     * @throws Exception if the test fails
     */
    @Test
    void updateQuizReturnsOk() throws Exception {
      mockMvc
          .perform(
              put("/api/quizzes/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(asJsonString(quizDTO))
                  .secure(true))
          .andExpect(status().isOk());
    }

    /**
     * This method tests the addUserToQuiz endpoint with a valid quiz ID and user ID.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 201 Created when the user is
     * successfully added to the quiz.
     *
     * @throws Exception if the test fails
     */
    @Test
    void addUserToQuizReturnsCreated() throws Exception {
      mockMvc.perform(post("/api/quizzes/1/users/1").secure(true)).andExpect(status().isCreated());
    }

    /**
     * This method tests the removeUserFromQuiz endpoint with a valid quiz ID and user ID.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 204 No Content when the user
     * is successfully removed from the quiz.
     *
     * @throws Exception if the test fails
     */
    @Test
    void removeUserFromQuizReturnsNoContent() throws Exception {
      mockMvc
          .perform(delete("/api/quizzes/1/users/1").secure(true))
          .andExpect(status().isNoContent());
    }

    /**
     * This method tests the getUsersByQuizId endpoint with a valid quiz ID.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the users are
     * successfully retrieved.
     *
     * @throws Exception if the test fails
     */
    @Test
    void getUsersByQuizIdReturnsOkAndUsers() throws Exception {
      mockMvc
          .perform(get("/api/quizzes/users/1").secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].id").value(1));
    }

    /**
     * This method tests the addTags endpoint with a valid quiz ID and body.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the tags are
     * successfully added to the quiz.
     *
     * @throws Exception if the test fails
     */
    @Test
    void addTagsReturnsOkAndQuiz() throws Exception {
      mockMvc
          .perform(
              patch("/api/quizzes/add/tags/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(asJsonString(tags))
                  .secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1));
    }

    /**
     * This method tests the deleteTags endpoint with a valid quiz ID and body.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the tags are
     * successfully deleted from the quiz.
     *
     * @throws Exception if the test fails
     */
    @Test
    void deleteTagsReturnsOkAndQuiz() throws Exception {
      mockMvc
          .perform(
              delete("/api/quizzes/delete/tags/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(asJsonString(tags))
                  .secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetQuizzesByTag() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      Pageable pageable = mock(Pageable.class);
      Page<QuizDTO> page = new PageImpl<>(new ArrayList<>());
      when(quizService.getQuizzesByTag(anyString(), eq(pageable))).thenReturn(page);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<Page<QuizDTO>> response = quizController.getQuizzesByTag("tag", pageable);

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    /**
     * This method tests the getQuizzesByCategory endpoint with a valid category and pageable.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the quizzes are
     * successfully retrieved.
     */
    @Test
    void testGetQuizzesByCategory() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      Pageable pageable = mock(Pageable.class);
      Page<QuizDTO> page = new PageImpl<>(new ArrayList<>());
      when(quizService.getQuizzesByCategory(anyString(), eq(pageable))).thenReturn(page);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<Page<QuizDTO>> response =
          quizController.getQuizzesByCategory("category", pageable);

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    /**
     * This method tests the updateTags endpoint with a valid quiz ID and list of tags.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the tags are
     * successfully updated.
     */
    @Test
    void testUpdateTags() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      QuizDTO quizDTO = mock(QuizDTO.class);
      when(quizService.updateTags(anyLong(), any())).thenReturn(quizDTO);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<QuizDTO> response = quizController.updateTags(1L, new ArrayList<>());

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    /**
     * This method tests the getAllTags endpoint.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the tags are
     * successfully retrieved.
     */
    @Test
    void testGetAllTags() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      List<Tag> tags = new ArrayList<>();
      when(quizService.getAllTags()).thenReturn(tags);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<List<Tag>> response = quizController.getAllTags();

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    /**
     * This method tests the getAllCategories endpoint.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the categories
     * are successfully retrieved.
     */
    @Test
    void testGetAllCategories() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      List<Category> categories = new ArrayList<>();
      when(quizService.getAllCategories()).thenReturn(categories);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<List<Category>> response = quizController.getAllCategories();

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    /**
     * This method tests the getAllPublicQuizzes endpoint.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the public
     * quizzes are successfully retrieved.
     */
    @Test
    void testGetAllPublicQuizzes() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      Pageable pageable = mock(Pageable.class);
      Page<QuizDTO> page = new PageImpl<>(new ArrayList<>());
      when(quizService.getAllPublicQuizzes(eq(pageable))).thenReturn(page);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<Page<QuizDTO>> response = quizController.getAllPublicQuizzes(pageable);

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.OK);
    }

    /**
     * This method tests the createCategory endpoint with a valid category.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 201 Created when the category
     * is successfully created.
     */
    @Test
    void testCreateCategory() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      Category category = mock(Category.class);
      when(quizService.createCategory(any())).thenReturn(category);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<Category> response = quizController.createCategory(category);

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.CREATED);
    }

    /**
     * This method tests the filterQuizzesByTags endpoint with a valid list of tags and pageable.
     *
     * <p>It verifies that the endpoint returns an HTTP status code of 200 OK when the quizzes are
     * successfully filtered.
     */
    @Test
    void testFilterQuizzesByTags() {
      // Mocking
      QuizService quizService = mock(QuizService.class);
      Pageable pageable = mock(Pageable.class);
      Page<QuizDTO> page = new PageImpl<>(new ArrayList<>());
      when(quizService.getQuizzesByTags(any(), eq(pageable))).thenReturn(page);

      // Testing
      QuizController quizController = new QuizController(quizService);
      ResponseEntity<Page<QuizDTO>> response =
          quizController.filterQuizzesByTags(new ArrayList<>(), pageable);

      // Verification
      assert (response.getStatusCode()).equals(HttpStatus.OK);
    }
  }

  /**
   * This method converts an object to a JSON string.
   *
   * @param obj the object to convert
   * @return the JSON string
   */
  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
