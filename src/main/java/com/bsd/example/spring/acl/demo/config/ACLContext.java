package com.bsd.example.spring.acl.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class ACLContext {
    private final DataSource dataSource;

    @Bean
    public ConcurrentMapCache userCacheBackend() {
        return new ConcurrentMapCache("userCache");
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ACL_ADMIN"));
    }


    @Bean
    public SpringCacheBasedAclCache aclCache(Cache cache, PermissionGrantingStrategy permissionGrantingStrategy,
                                             AclAuthorizationStrategy aclAuthorizationStrategy) {
        return new SpringCacheBasedAclCache(cache, permissionGrantingStrategy, aclAuthorizationStrategy);
    }

    @Bean
    public BasicLookupStrategy lookupStrategy(AclCache aclCache, AclAuthorizationStrategy aclAuthorizationStrategy,
                                              PermissionGrantingStrategy permissionGrantingStrategy) {
        return new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy, permissionGrantingStrategy);
    }

    @Bean
    public JdbcMutableAclService aclService(BasicLookupStrategy lookupStrategy, AclCache aclCache) {
        JdbcMutableAclService jdbcMutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
        jdbcMutableAclService.setSidIdentityQuery("SELECT currval('acl_sid_id_seq')");
        jdbcMutableAclService.setClassIdentityQuery("SELECT currval('acl_class_id_seq')");
        return jdbcMutableAclService;
    }

//    @Bean
//    public AclPermissionEvaluator aclPermissionEvaluator(JdbcMutableAclService jdbcMutableAclService) {
//        return new AclPermissionEvaluator(jdbcMutableAclService);
//    }
}
