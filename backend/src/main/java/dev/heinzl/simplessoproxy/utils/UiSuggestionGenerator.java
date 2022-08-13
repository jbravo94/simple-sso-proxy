package dev.heinzl.simplessoproxy.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        String errorMessage = "Provide absolute valid path as first argument";

        if (args.length == 0) {
            throw new IllegalStateException(errorMessage);
        }

        String directory = args[0];

        Path path = Paths.get(directory);

        if (!Files.exists(path)) {
            throw new IllegalStateException(errorMessage);
        }

        UiSuggestionGenerator.generate(directory);
    }

    public static void generate(String directory) throws IOException {

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

        File file = Paths.get(directory).resolve(fileName).toFile();

        FileUtils.writeStringToFile(file, fileContent, Charsets.UTF_8);
    }
}
