export class GridElement {

    title: string;
    description: string;
    url: string;
    isRouterLink: boolean;

    constructor(title: string, description: string, url: string, isRouterLink?: boolean) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.isRouterLink = isRouterLink ? isRouterLink : true;
    }

}
