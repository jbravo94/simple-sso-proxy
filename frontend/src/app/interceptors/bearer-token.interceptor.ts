import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LocalTokenService } from '../services/local-token/local-token.service';

@Injectable()
export class BearerTokenInterceptor implements HttpInterceptor {

  constructor(private localTokenService: LocalTokenService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const bearerToken = this.localTokenService.getProxyCookie();

    if (bearerToken) {
      const modifiedReq = req.clone({ setHeaders: { Authorization: `Bearer ${bearerToken}` } });

      return next.handle(modifiedReq);
    } else {
      return next.handle(req);
    }
  }
}