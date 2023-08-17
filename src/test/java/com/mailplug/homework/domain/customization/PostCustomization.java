package com.mailplug.homework.domain.customization;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.post.persistence.Post;

public class PostCustomization implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return ((query, context) -> query.getType().equals(Post.class)
                ? new ObjectContainer(factory(context))
                : generator.generate(query, context));
    }

    static Post factory(final ObjectGenerationContext context) {

        return new Post(
                "title",
                "content",
                MemberCustomization.factory(context),
                new Board(BoardType.FREE)
        );
    }
}
