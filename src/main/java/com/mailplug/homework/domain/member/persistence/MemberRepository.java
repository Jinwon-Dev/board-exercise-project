package com.mailplug.homework.domain.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT member FROM Member member" +
            " WHERE member.email = :email")
    Optional<Member> findMemberByEmail(@Param("email") final String email);

    @Query("SELECT CASE WHEN count(member) > 0" +
            " THEN true ELSE false END FROM Member member" +
            " WHERE member.email = :email")
    boolean existsByEmail(@Param("email") final String email);
}
