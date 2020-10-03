package com.github.uuidcode.codewriter;

import java.io.File;

import org.junit.Test;

public class ReplaceFileContentBuilderTest {
    @Test
    public void test() {
        ReplaceFileContentBuilder.of()
            .setSourceFile(new File("src/test/java/com/github/uuidcode/codewriter/controller/IssueController.java"))
            .setReplaceFilePath(true)
            .replace("codewriter", "sample")
            .replace("IssueController", "CommentController")
            .build();
    }
}