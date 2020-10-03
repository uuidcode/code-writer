package com.github.uuidcode.codewriter;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Data(staticConstructor = "of")
@Accessors(chain = true)
public class ReplaceFileContentBuilder {
    @Setter(PRIVATE)
    @Getter(PRIVATE)
    private ReplaceContentBuilder replaceContentBuilder = ReplaceContentBuilder.of();
    private File sourceFile;
    private File targetFile;

    public ReplaceFileContentBuilder replace(String target, String replacement) {
        this.replaceContentBuilder.replace(target, replacement);
        return this;
    }

    public void build() {
        if (this.replaceContentBuilder == null) {
            return;
        }

        if (this.targetFile == null) {
            return;
        }

        if (this.sourceFileIsExists()) {
            this.replace();
        }
    }

    private boolean sourceFileIsExists() {
        return ofNullable(this.sourceFile)
            .filter(File::exists)
            .filter(File::isFile)
            .isPresent();
    }

    private void replace() {
        String content = getContent(this.sourceFile);

        String newContent = this.replaceContentBuilder
            .setContent(content)
            .build();

        setContent(this.targetFile, newContent);
    }

    public String getContent(File file) {
        try {
            return FileUtils.readFileToString(file, defaultCharset());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setContent(File file, String data) {
        try {
            FileUtils.writeStringToFile(file, data, defaultCharset());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
