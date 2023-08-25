package com.bsd.example.spring.acl.demo.init;

import com.bsd.example.spring.acl.demo.domain.Post;
import com.bsd.example.spring.acl.demo.domain.User;
import com.bsd.example.spring.acl.demo.service.PostService;
import com.bsd.example.spring.acl.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitApplicationRunner implements ApplicationRunner {
    private final UserService userService;
    private final PostService postService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Init");
//        User user = userService.save(new User(null, "user1", "testPassword"));
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        Post post = postService.save(new Post(null, "Test Post 1"));
//        log.debug("Post: {}", post);
//        postService.delete(post.getId());
    }
}
