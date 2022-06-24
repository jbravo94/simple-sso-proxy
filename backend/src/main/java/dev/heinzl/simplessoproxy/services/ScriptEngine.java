package dev.heinzl.simplessoproxy.services;

import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.heinzl.simplessoproxy.repositories.CredentialsRepository;
import dev.heinzl.simplessoproxy.scripting.ScriptingApi;

@Service
public class ScriptEngine {

    ScriptEngineManager factory = new ScriptEngineManager();
    javax.script.ScriptEngine engine = factory.getEngineByName("groovy");

    CredentialsRepository credentialsRepository;

    @Autowired
    public ScriptEngine(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    String template = """
            def script(scriptingApi) {
                %s
            }
            """;

    public void applyScript(String script, ScriptingApi scriptingApi) {

        // credentialsRepository.save(new Credential("TEst", 2));

        try {
            engine.eval(String.format(template, script));
            Invocable inv = (Invocable) engine;
            Object[] params = { scriptingApi };

            Object result = inv.invokeFunction("script", params);

            System.out.println(result);
        } catch (NoSuchMethodException | ScriptException e) {
            e.printStackTrace();
        }
    }
}
