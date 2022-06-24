export class App {

    name: string;
    baseUrl: string;
    loginScript: string;
    logoutScript: string;
    resetScript: string;

    constructor(name: string, baseUrl: string, loginScript: string, logoutScript: string, resetScript: string) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.loginScript = loginScript;
        this.logoutScript = logoutScript;
        this.resetScript = resetScript;
    }
}