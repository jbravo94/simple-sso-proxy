import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { BACKEND_URL, IS_DEV } from 'src/app/globals';
import { App } from 'src/app/models/app';
import { LocalTokenService } from '../local-token/local-token.service';

@Injectable({
  providedIn: 'root'
})
export class AppsService {

  constructor(private http: HttpClient, private localTokenService: LocalTokenService) { }

  getApps(): Observable<App[]> {
    if (IS_DEV) {
      return of([
        { id: '62b62b51e34f9d457dff6f72', name: 'OpenMRS', baseUrl: 'https://demo.mybahmni.org/openmrs/', loginScript: '', logoutScript: '', resetScript: '', proxyScript: '' },
        { id: '62b62b51e34f9d457dff6f73', name: 'odoo', baseUrl: 'https://erp-demo.mybahmni.org/', loginScript: '', logoutScript: '', resetScript: '', proxyScript: '' },
      ]);
    } else {
      return this.http.get<App[]>(BACKEND_URL + '/api/v1/apps/all');
    }
  }

  postApp(app: App): Observable<App> {
    return this.http.post<App>(BACKEND_URL + '/api/v1/apps', app);
  }

  editApp(app: App): Observable<App> {
    return this.http.put<App>(BACKEND_URL + '/api/v1/apps/' + app.id, app);
  }

  deleteApp(app: App): Observable<App> {
    return this.http.delete<App>(BACKEND_URL + '/api/v1/apps/' + app.id);
  }
}
