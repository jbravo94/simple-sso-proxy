export class JwtToken {

    iss: string;
    aud: string;
    sub: number;
    exp: number;
    roles: string[];

    constructor(iss: string, aud: string, sub: number, exp: number, roles: string[]) {
        this.iss = iss;
        this.aud = aud;
        this.sub = sub;
        this.exp = exp;
        this.roles = roles;
    }

}
