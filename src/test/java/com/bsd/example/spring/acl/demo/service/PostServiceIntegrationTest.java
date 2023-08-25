package com.bsd.example.spring.acl.demo.service;

import com.bsd.example.spring.acl.demo.domain.Post;
import com.bsd.example.spring.acl.demo.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class PostServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = userService.save(new User("testUser1", "password"));
        user2 = userService.save(new User("testUser2", "password"));
    }

    @Test
    @WithMockUser
    void test_PostServiceSave() {
        assertDoesNotThrow(() -> postService.save(new Post(null, "Test Post 1")));
    }

    @Test
    @WithMockUser(username = "testUser1")
    void test_PostServiceFindById() {
        Post post = postService.save(new Post(null, "Test Post 2"));
        log.debug("Post Id: {}", post.getId());
        assertDoesNotThrow(() -> postService.find(post.getId()));
    }

    @Test
    @WithMockUser(username = "testUser3")
    void test_FindById_Failure() {
        assertThrows(AccessDeniedException.class, () -> postService.find(5));
    }

    @Test
    @WithMockUser(username = "testUser1")
    void test_Delete() {
        Post post = postService.save(new Post(null, "Test Post 3"));
        log.debug("Post Id: {}", post.getId());
        assertDoesNotThrow(() -> postService.delete(post.getId()));
    }
}