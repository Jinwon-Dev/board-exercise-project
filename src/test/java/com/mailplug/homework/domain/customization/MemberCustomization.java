package com.mailplug.homework.domain.customization;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import com.mailplug.homework.domain.member.persistence.Member;

import java.util.Random;

public class MemberCustomization implements Customizer {

    @Override
    public ObjectGenerator customize(final ObjectGenerator generator) {

        return ((query, context) -> query.getType().equals(Member.class)
                ? new ObjectContainer(factory(context))
                : generator.generate(query, context));
    }

    static Member factory(final ObjectGenerationContext context) {

        final Random random = new Random();
        final String email = "test" + random.nextInt(1_000_000) + "@gmail.com";

        return new Member(
                email,
                "test1234"
        );
    }
}
