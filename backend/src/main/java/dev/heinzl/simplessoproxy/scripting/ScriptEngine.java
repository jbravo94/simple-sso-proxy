/*
 * The MIT License
 * Copyright © 2022 Johannes HEINZL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dev.heinzl.simplessoproxy.scripting;

import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.stereotype.Service;

import dev.heinzl.simplessoproxy.scripting.api.ScriptingApi;

@Service
public class ScriptEngine {

    ScriptEngineManager factory = new ScriptEngineManager();
    javax.script.ScriptEngine engine = factory.getEngineByName("groovy");

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
            throw new IllegalStateException("The script threw an error. Refer to the logs.", e);
        }
    }
}
