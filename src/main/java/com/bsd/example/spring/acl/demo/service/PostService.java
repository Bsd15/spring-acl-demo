package com.bsd.example.spring.acl.demo.service;

import com.bsd.example.spring.acl.demo.domain.Post;
import com.bsd.example.spring.acl.demo.domain.User;
import com.bsd.example.spring.acl.demo.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final MutableAclService aclService;

    @Transactional
    public Post save(Post post) {
        Post savedPost = postRepository.save(post);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(savedPost);
        Sid sid = new PrincipalSid(auth.getName());
        MutableAcl mutableAcl = null;
        try {
            mutableAcl = (MutableAcl) aclService.readAclById(objectIdentity);
        } catch (NotFoundException notFoundException) {
            log.error("ACL not found. Creating one.");
            mutableAcl = aclService.createAcl(objectIdentity);
        }

        mutableAcl.insertAce(mutableAcl.getEntries().size(), BasePermission.CREATE, sid, true);
        mutableAcl.insertAce(mutableAcl.getEntries().size(), BasePermission.READ, sid, true);
        mutableAcl.insertAce(mutableAcl.getEntries().size(), BasePermission.WRITE, sid, true);
        mutableAcl.insertAce(mutableAcl.getEntries().size(), BasePermission.DELETE, sid, true);
        aclService.updateAcl(mutableAcl);
        return savedPost;
    }

    @PreAuthorize("hasPermission(#id, 'com.bsd.example.spring.acl.demo.domain.Post', 'READ')")
    public Post find(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new RuntimeException("Post not found"); //NOSONAR
        }
        return post.get();
    }

    @PreAuthorize("hasRole('USER')")
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @PreAuthorize("hasPermission(#id, 'com.bsd.example.spring.acl.demo.domain.Post', 'DELETE')")
    public void delete(long id) {
        postRepository.deleteById(id);
    }
}
