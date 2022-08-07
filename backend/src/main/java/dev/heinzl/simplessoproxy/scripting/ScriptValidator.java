package dev.heinzl.simplessoproxy.scripting;

import org.apache.groovy.parser.antlr4.util.StringUtils;

import dev.heinzl.simplessoproxy.utils.Algorithms;
import dev.heinzl.simplessoproxy.utils.Validator;

public class ScriptValidator implements Validator<String> {

    @Override
    public boolean isValid(String entity) {
        return staticCodeAnalysis(entity);
    }

    private boolean staticCodeAnalysis(String entity) {

        if (StringUtils.isEmpty(entity)) {
            return true;
        }

        return Algorithms.containsBalancedBrackets(entity);
    }

    private ScriptValidator() {
    }

    private static ScriptValidator scriptValidator;

    public static ScriptValidator getInstance() {
        if (scriptValidator == null) {
            scriptValidator = new ScriptValidator();
        }
        return scriptValidator;
    }
}
