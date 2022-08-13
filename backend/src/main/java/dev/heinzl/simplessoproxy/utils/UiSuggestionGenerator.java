package dev.heinzl.simplessoproxy.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.base.Charsets;

import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;
import lombok.Generated;

public class UiSuggestionGenerator {

    @Generated
    public static void main(String[] args) throws IOException {
        UiSuggestionGenerator.generate();
    }

    public static void generate() throws IOException {

        String fileName = "scripting-api-suggestions.ts";

        JSONArray suggestions = new JSONArray();

        for (Method m : ScriptingApi.class.getDeclaredMethods()) {

            String preview = m.getName() + "("
                    + Stream.of(m.getParameters()).map(p -> p.getType().getSimpleName() + " " + p.getName())
                            .collect(Collectors.joining(", "))
                    + ")";

            String suggestion = m.getName() + "("
                    + Stream.of(m.getParameters()).map(p -> p.getName()).collect(Collectors.joining(", ")) + ")";

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("label", m.getName());
            jsonObject.put("kind", 1);
            jsonObject.put("detail", preview);
            jsonObject.put("insertText", suggestion);

            suggestions.put(jsonObject);
        }

        String sanitizedJSONString = suggestions.toString(4).replaceAll("1",
                "monaco.languages.CompletionItemKind.Function");

        String fileContent = "export const scriptingApiSuggestions = " + sanitizedJSONString + ";";

        File file = new File(fileName);
        FileUtils.writeStringToFile(file, fileContent, Charsets.UTF_8);
    }
}
