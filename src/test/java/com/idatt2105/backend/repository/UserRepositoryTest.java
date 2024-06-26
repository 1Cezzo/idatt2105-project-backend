package com.idatt2105.backend.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.idatt2105.backend.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The UserRepositoryTest class is a test class that tests the UserRepository class. */
@DataJpaTest
public class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  /**
   * Test the findByUsername method with an existing username. It verifies that the method returns
   * the correct user.
   */
  @Test
  public void testFindByUsername_ExistingUsername_ReturnsUser() {
    // Given
    User user = new User("testuser", "password");
    userRepository.save(user);

    // When
    Optional<User> optionalUser = userRepository.findByUsername("testuser");

    // Then
    assertTrue(optionalUser.isPresent());
    assertEquals("testuser", optionalUser.get().getUsername());
  }

  /**
   * Test the findByUsername method with a non-existing username. It verifies that the method
   * returns an empty optional.
   */
  @Test
  public void testFindByUsername_NonExistingUsername_ReturnsEmptyOptional() {
    // When
    Optional<User> optionalUser = userRepository.findByUsername("nonexistent");

    // Then
    assertFalse(optionalUser.isPresent());
  }

  /**
   * Test the CRUD operations of the UserRepository. It verifies that the operations are successful.
   */
  @Test
  public void testCRUD() {
    // Create
    User user = new User("testuser", "password");
    userRepository.save(user);
    assertEquals(1, userRepository.count());

    // Read
    Optional<User> optionalUser = userRepository.findByUsername("testuser");
    assertTrue(optionalUser.isPresent());
    assertEquals("testuser", optionalUser.get().getUsername());

    // Update
    user.setUsername("newuser");
    userRepository.save(user);
    assertEquals(1, userRepository.count());
    optionalUser = userRepository.findByUsername("newuser");
    assertTrue(optionalUser.isPresent());
    assertEquals("newuser", optionalUser.get().getUsername());

    // Delete
    userRepository.delete(user);
    assertEquals(0, userRepository.count());
  }
}
