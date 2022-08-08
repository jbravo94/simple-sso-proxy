export const scriptingApiSuggestions = [
    {
        label: "addProxyRequestHeaderIfNotPreset",
        kind: monaco.languages.CompletionItemKind.Function,
        detail: "void addProxyRequestHeaderIfNotPreset(String key, String value)",
        insertText: "addProxyRequestHeaderIfNotPreset(\"key\", \"value\")"
    },
    {
        label: "addProxyCookieIfNotPreset",
        kind: monaco.languages.CompletionItemKind.Function,
        detail: "void addProxyCookieIfNotPreset(String name, String value)",
        insertText: "addProxyCookieIfNotPreset(\"name\", \"value\")"
    },
    {
        label: "getUsername",
        kind: monaco.languages.CompletionItemKind.Function,
        detail: "String getUsername()",
        insertText: "getUsername()"
    },
    {
        label: "getPassword",
        kind: monaco.languages.CompletionItemKind.Function,
        detail: "String getPassword()",
        insertText: "getPassword()"
    },
];
