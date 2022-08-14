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
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { BACKEND_URL, IS_DEV } from 'src/app/globals/globalConstants';
import { App } from 'src/app/apps/app';

@Injectable({
  providedIn: 'root'
})
export class AppsService {

  constructor(private http: HttpClient) { }

  getApps(): Observable<App[]> {
    if (IS_DEV) {
      return of([
        { id: '62b62b51e34f9d457dff6f72', name: 'OpenMRS', baseUrl: 'https://demo.mybahmni.org/openmrs/', proxyUrl: 'http:/localhost:4200/openmrs/', loginScript: '', logoutScript: '', resetScript: '', proxyScript: '' },
        { id: '62b62b51e34f9d457dff6f73', name: 'odoo', baseUrl: 'https://erp-demo.mybahmni.org/web', proxyUrl: 'http:/localhost:4200/web/', loginScript: '', logoutScript: '', resetScript: '', proxyScript: '' },
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
