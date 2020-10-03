package com.github.uuidcode.codewriter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class ReplaceContentBuilderTest {
    @Test
    public void test() {
        {
            ReplaceContentBuilder builder =
                ReplaceContentBuilder.of();

            assertThat(builder.build()).isNull();
        }

        {
            String content = ReplaceContentBuilder.of()
                .setContent("Hello, ${name}!")
                .replace("${name}", "World")
                .build();


            assertThat(content).isEqualTo("Hello, World!");
        }
    }
}