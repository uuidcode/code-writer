package com.github.uuidcode.codewriter;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static lombok.AccessLevel.PRIVATE;

@Data(staticConstructor = "of")
@Accessors(chain = true)
public class ReplaceContentBuilder {
    @Setter(PRIVATE)
    @Getter(PRIVATE)
    private Map<String, String> map = new LinkedHashMap<>();

    @Setter(PRIVATE)
    @Getter(PRIVATE)
    private String newContent;

    private String content;

    public ReplaceContentBuilder replace(String target, String replacement) {
        this.map.put(target, replacement);
        return this;
    }

    public ReplaceContentBuilder replace(String target, int replacement) {
        return this.replace(target, String.valueOf(replacement));
    }

    public String build() {
        if (this.content == null) {
            return null;
        }

        this.newContent = this.content;

        this.replace();

        return this.newContent;
    }

    private void replace() {
        if (this.map == null) {
            return;
        }

        this.map.entrySet()
            .forEach(this::replace);
    }

    private void replace(Map.Entry<String, String> entry) {
        String key = entry.getKey();
        String value = entry.getValue();

        if (value == null) {
            return;
        }

        this.newContent = this.newContent.replace(key, value);
    }
}
