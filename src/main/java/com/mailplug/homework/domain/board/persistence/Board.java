package com.mailplug.homework.domain.board.persistence;

import com.mailplug.homework.domain.BaseTimeEntity;
import com.mailplug.homework.domain.post.persistence.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Board extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType name;

    @OneToMany(mappedBy = "board", cascade = REMOVE)
    private Set<Post> posts = new HashSet<>();

    @Builder
    public Board(final BoardType name) {
        this.name = name;
    }
}
