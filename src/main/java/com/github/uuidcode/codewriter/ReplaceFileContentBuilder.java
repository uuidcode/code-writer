package com.github.uuidcode.codewriter;

import java.io.File;

import org.apache.commons.io.FileUtils;

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
    private ReplaceContentBuilder replaceContentBuilder =
        ReplaceContentBuilder.of();

    private File sourceFile;
    private File targetFile;
    private boolean replaceFilePath;

    public ReplaceFileContentBuilder replace(String target, String replacement) {
        this.replaceContentBuilder.replace(target, replacement);
        return this;
    }

    public void build() {
        if (this.replaceContentBuilder == null) {
            return;
        }

        if (this.targetFile == null) {
            if (!this.replaceFilePath) {
                return;
            }

            this.createTargetFile();
        }

        if (this.sourceFileExists()) {
            this.replace();
        }
    }

    private void createTargetFile() {
        try {
            String targetFilePath = this.replaceContentBuilder
                .setContent(this.sourceFile.getCanonicalPath())
                .build();

            this.targetFile = new File(targetFilePath);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private boolean sourceFileExists() {
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
