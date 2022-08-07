export class App {

    id: string;
    name: string;
    baseUrl: string;
    proxyUrl: string;
    loginScript: string;
    logoutScript: string;
    resetScript: string;
    proxyScript: string;

    constructor(id: string, name: string, baseUrl: string, proxyUrl: string, loginScript: string, logoutScript: string, resetScript: string, proxyScript: string) {
        this.id = id;
        this.name = name;
        this.baseUrl = baseUrl;
        this.proxyUrl = proxyUrl;
        this.loginScript = loginScript;
        this.logoutScript = logoutScript;
        this.resetScript = resetScript;
        this.proxyScript = proxyScript;
    }
}