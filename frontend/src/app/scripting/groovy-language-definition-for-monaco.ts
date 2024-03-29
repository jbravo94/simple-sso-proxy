/*
The MIT License
Copyright © 2022 Johannes HEINZL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
import { scriptingApiSuggestions } from "./scripting-api-suggestions";

function getTokens(tokens: string, divider = "|"): string[] {
    return tokens.split(divider);
}

const wordPattern = /(-?\d*\.\d\w*)|([^`~!@#%^&*()\-=+[{\]}\\|;:'",./?\s]+)/g;

const bracketTokens = [
    {
        open: "[",
        close: "]",
        token: "delimiter.square",
    },
    {
        open: "(",
        close: ")",
        token: "delimiter.parenthesis",
    },
    {
        open: "{",
        close: "}",
        token: "delimiter.curly",
    },
];

const autoClosingPairs = [
    { open: "{", close: "}" },
    { open: "[", close: "]" },
    { open: "(", close: ")" },
    { open: '"', close: '"' },
    { open: "'", close: "'" },
    { open: "`", close: "`" },
];

const surroundingPairs = autoClosingPairs;

const id = "groovy";
const label = "Groovy";

export const registerGroovyLanguageForMonaco = (languages: any) => {
    const brackets = [
        ["{", "}"],
        ["[", "]"],
        ["(", ")"],
    ];

    languages.register({ id, aliases: [label] });

    languages.setMonarchTokensProvider(id, {
        brackets: bracketTokens,
        tokenPostfix: ".groovy",
        keywords: getTokens(
            "assert|with|abstract|continue|for|new|switch|assert|default|goto|package|synchronized|boolean|do|if|private|this|break|double|implements|protected|throw|byte|else|import|public|throws|case|enum|instanceof|return|transient|catch|extends|int|short|try|char|final|interface|static|void|class|finally|long|strictfp|volatile|def|float|native|super|while|in|as"
        ),
        typeKeywords: getTokens(
            "Long|Integer|Short|Byte|Double|Number|Float|Character|Boolean|StackTraceElement|Appendable|StringBuffer|Iterable|ThreadGroup|Runnable|Thread|IllegalMonitorStateException|StackOverflowError|OutOfMemoryError|VirtualMachineError|ArrayStoreException|ClassCastException|LinkageError|NoClassDefFoundError|ClassNotFoundException|RuntimeException|Exception|ThreadDeath|Error|Throwable|System|ClassLoader|Cloneable|Class|CharSequence|Comparable|String|Object"
        ),
        constants: getTokens("null|Infinity|NaN|undefined|true|false"),
        builtinFunctions: getTokens(
            "AbstractMethodError|AssertionError|ClassCircularityError|ClassFormatError|Deprecated|EnumConstantNotPresentException|ExceptionInInitializerError|IllegalAccessError|IllegalThreadStateException|InstantiationError|InternalError|NegativeArraySizeException|NoSuchFieldError|Override|Process|ProcessBuilder|SecurityManager|StringIndexOutOfBoundsException|SuppressWarnings|TypeNotPresentException|UnknownError|UnsatisfiedLinkError|UnsupportedClassVersionError|VerifyError|InstantiationException|IndexOutOfBoundsException|ArrayIndexOutOfBoundsException|CloneNotSupportedException|NoSuchFieldException|IllegalArgumentException|NumberFormatException|SecurityException|Void|InheritableThreadLocal|IllegalStateException|InterruptedException|NoSuchMethodException|IllegalAccessException|UnsupportedOperationException|Enum|StrictMath|Package|Compiler|Readable|Runtime|StringBuilder|Math|IncompatibleClassChangeError|NoSuchMethodError|ThreadLocal|RuntimePermission|ArithmeticException|NullPointerException"
        ),
        operators: [
            ".",
            ".&",
            ".@",
            "?.",
            "*",
            "*.",
            "*:",
            "~",
            "!",
            "++",
            "--",
            "**",
            "+",
            "-",
            "*",
            "/",
            "%",
            "<<",
            ">>",
            ">>>",
            "..",
            "..<",
            "<",
            "<=",
            ">",
            ">",
            "==",
            "!=",
            "<=>",
            "===",
            "!==",
            "=~",
            "==~",
            "^",
            "|",
            "&&",
            "||",
            "?",
            ":",
            "?:",
            "=",
            "**=",
            "*=",
            "/=",
            "%=",
            "+=",
            "-=",
            "<<=",
            ">>=",
            ">>>=",
            "&=",
            "^=",
            "|=",
            "?=",
        ],
        symbols: /[=><!~?:&|+\-*/^%]+/,
        escapes: /\\(?:[abfnrtv\\"'`]|x[0-9A-Fa-f]{1,4}|u[0-9A-Fa-f]{4}|U[0-9A-Fa-f]{8})/,

        regexpctl: /[(){}[\]$^|\-*+?.]/,
        regexpesc: /\\(?:[bBdDfnrstvwWn0\\/]|@regexpctl|c[A-Z]|x[0-9a-fA-F]{2}|u[0-9a-fA-F]{4})/,

        tokenizer: {
            root: [
                { include: "@whitespace" },
                [
                    /\/(?=([^\\/]|\\.)+\/([dgimsuy]*)(\s*)(\.|;|,|\)|\]|\}|$))/,
                    { token: "regexp", bracket: "@open", next: "@regexp" },
                ],
                { include: "@comments" },
                { include: "@numbers" },
                { include: "common" },
                [/[;,.]/, "delimiter"],
                [/[(){}[\]]/, "@brackets"],
                [
                    /[a-zA-Z_$]\w*/,
                    {
                        cases: {
                            "@keywords": "keyword",
                            "@typeKeywords": "type",
                            "@constants": "constant.groovy",
                            "@builtinFunctions": "constant.other.color",
                            "@default": "identifier",
                        },
                    },
                ],
                [
                    /@symbols/,
                    {
                        cases: {
                            "@operators": "operator",
                            "@default": "",
                        },
                    },
                ],
            ],
            common: [
                // delimiters and operators
                [/[()[\]]/, "@brackets"],
                [/[<>](?!@symbols)/, "@brackets"],
                [
                    /@symbols/,
                    {
                        cases: {
                            "@operators": "delimiter",
                            "@default": "",
                        },
                    },
                ],

                [
                    /\/(?=([^\\/]|\\.)+\/([gimsuy]*)(\s*)(\.|;|\/|,|\)|\]|\}|$))/,
                    { token: "regexp", bracket: "@open", next: "@regexp" },
                ],

                // delimiter: after number because of .\d floats
                [/[;,.]/, "delimiter"],

                // strings
                [/"([^"\\]|\\.)*$/, "string.invalid"],
                [/'([^'\\]|\\.)*$/, "string.invalid"],
                [/"/, "string", "@string_double"],
                [/'/, "string", "@string_single"],
            ],
            whitespace: [[/\s+/, "white"]],
            comments: [
                [/\/\/.*/, "comment"],
                [
                    /\/\*/,
                    {
                        token: "comment.quote",
                        next: "@comment",
                    },
                ],
            ],
            comment: [
                [/[^*/]+/, "comment"],
                [
                    /\*\//,
                    {
                        token: "comment.quote",
                        next: "@pop",
                    },
                ],
                [/./, "comment"],
            ],
            commentAnsi: [
                [
                    /\/\*/,
                    {
                        token: "comment.quote",
                        next: "@comment",
                    },
                ],
                [/[^*/]+/, "comment"],
                [
                    /\*\//,
                    {
                        token: "comment.quote",
                        next: "@pop",
                    },
                ],
                [/./, "comment"],
            ],
            numbers: [
                [/[+-]?\d+(?:(?:\.\d*)?(?:[eE][+-]?\d+)?)?f?\b/, "number.float"],
                [/[+-]?(?:0[obx])?\d+(?:u?[lst]?)?\b/, "number"],
            ],
            regexp: [
                [/(\{)(\d+(?:,\d*)?)(\})/, ["regexp.escape.control", "regexp.escape.control", "regexp.escape.control"]],
                [
                    /(\[)(\^?)(?=(?:[^\]\\/]|\\.)+)/,
                    // @ts-ignore
                    ["regexp.escape.control", { token: "regexp.escape.control", next: "@regexrange" }],
                ],
                [/(\()(\?:|\?=|\?!)/, ["regexp.escape.control", "regexp.escape.control"]],
                [/[()]/, "regexp.escape.control"],
                [/@regexpctl/, "regexp.escape.control"],
                [/[^\\/]/, "regexp"],
                [/@regexpesc/, "regexp.escape"],
                [/\\\./, "regexp.invalid"],
                // @ts-ignore
                [/(\/)([gimsuy]*)/, [{ token: "regexp", bracket: "@close", next: "@pop" }, "keyword.other"]],
            ],

            regexrange: [
                [/-/, "regexp.escape.control"],
                [/\^/, "regexp.invalid"],
                [/@regexpesc/, "regexp.escape"],
                [/[^\]]/, "regexp"],
                [/\]/, { token: "regexp.escape.control", next: "@pop", bracket: "@close" }],
            ],
            embedded: [
                [
                    /([^@]|^)([@]{4})*[@]{2}([@]([^@]|$)|[^@]|$)/,
                    {
                        token: "@rematch",
                        next: "@pop",
                        nextEmbedded: "@pop",
                    },
                ],
            ],
            string_double: [
                [/\$\{/, { token: "delimiter.bracket", next: "@bracketCounting" }],
                [/[^\\"$]+/, "string"],
                [/[^\\"]+/, "string"],
                [/@escapes/, "string.escape"],
                [/\\./, "string.escape.invalid"],
                [/"/, "string", "@pop"],
            ],
            string_single: [
                [/[^\\']+/, "string"],
                [/@escapes/, "string.escape"],
                [/\\./, "string.escape.invalid"],
                [/'/, "string", "@pop"],
            ],
            string_backtick: [
                [/\$\{/, { token: "delimiter.bracket", next: "@bracketCounting" }],
                [/[^\\"$]+/, "string"],
                [/@escapes/, "string.escape"],
                [/\\./, "string.escape.invalid"],
                [/"/, "string", "@pop"],
            ],
            bracketCounting: [
                [/\{/, "delimiter.bracket", "@bracketCounting"],
                [/\}/, "delimiter.bracket", "@pop"],
                { include: "common" },
            ],
        },
    });
    languages.setLanguageConfiguration(id, {
        comments: {
            lineComment: "//",
            blockComment: ["/*", "*/"],
        },
        brackets,
        autoClosingPairs,
        surroundingPairs,
        wordPattern,
    });

    monaco.languages.registerCompletionItemProvider('groovy', {
        // Run this function when the period or open parenthesis is typed (and anything after a space)
        triggerCharacters: ['.'],

        // Function to generate autocompletion results
        provideCompletionItems: function (model, position, token) {

            const last_chars = model.getValueInRange({ startLineNumber: position.lineNumber, startColumn: 0, endLineNumber: position.lineNumber, endColumn: position.column });
            const words = last_chars.replace("\t", "").split(" ");

            if (!words || words.length === 0) {
                return {
                    suggestions: []
                };
            }

            const lastWord = words[words.length - 1]

            if (lastWord !== 'scriptingApi.') {
                return {
                    suggestions: []
                };
            }

            const results: any = scriptingApiSuggestions;

            return {
                suggestions: results
            };
        },
    });

    monaco.languages.registerCompletionItemProvider('groovy', {
        provideCompletionItems: function (model, position, token) {

            const results: any = [
                {
                    label: "scriptingApi",
                    detail: "Type . after this keyword for API suggestions.",
                    insertText: "scriptingApi"
                }
            ];

            return {
                suggestions: results
            };
        },
    });
}