package dev.heinzl.simplessoproxy.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;

public class UiSuggestionGenerator {
    public static void generate() {

        List<String> suggestions = new ArrayList<>();

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

            String str = jsonObject.toString(4);

            String replaceAll = str.replaceAll("1", "monaco.languages.CompletionItemKind.Function");

            suggestions.add(replaceAll);
        }

        String r = "[\n" + suggestions.stream().collect(Collectors.joining(",\n")) + "\n]";

        System.out.print(r);
    }
}
