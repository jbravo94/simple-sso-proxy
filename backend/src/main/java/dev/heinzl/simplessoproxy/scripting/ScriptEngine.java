package dev.heinzl.simplessoproxy.scripting;

import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;
import dev.heinzl.simplessoproxy.users.UsersRepository;

@Service
public class ScriptEngine {

    ScriptEngineManager factory = new ScriptEngineManager();
    javax.script.ScriptEngine engine = factory.getEngineByName("groovy");

    @Autowired
    UsersRepository usersRepository;

    String template = """
            import java.net.http.*
            import org.apache.commons.lang3.*
            import org.apache.commons.collections4.*
            import dev.heinzl.simplessoproxy.scripting.api.*

            def script(scriptingApi) {
                %s
            }
            """;

    public void applyScript(String script, ScriptingApi scriptingApi) {

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
