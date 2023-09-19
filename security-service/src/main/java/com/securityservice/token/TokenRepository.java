package com.securityservice.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("""
    select t from Token t inner join t.user u
    where u.id = :userId and (t.expired = false or t.revoked = false)
        """)
    List<Token> findAllValidTokensByUser(Long userId);
    Optional<Token> findByToken(String token);

}
