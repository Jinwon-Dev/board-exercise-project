package com.mailplug.homework.domain.member.persistence;

import com.mailplug.homework.domain.BaseTimeEntity;
import com.mailplug.homework.domain.post.persistence.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", cascade = REMOVE)
    private Set<Post> posts = new HashSet<>();

    @Builder
    public Member(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
