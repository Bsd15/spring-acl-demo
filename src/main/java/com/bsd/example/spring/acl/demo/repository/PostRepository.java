package com.bsd.example.spring.acl.demo.repository;

import com.bsd.example.spring.acl.demo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
