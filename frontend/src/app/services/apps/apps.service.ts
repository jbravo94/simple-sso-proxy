import { Injectable } from '@angular/core';
import { App } from 'src/app/models/app';

@Injectable({
  providedIn: 'root'
})
export class AppsService {

  constructor() { }

  getApps(): App[] {
    return [
      { name: 'OpenMRS', baseUrl: 'https://demo.mybahmni.org/openmrs/', loginScript: '', logoutScript: '', resetScript: '' },
      { name: 'odoo', baseUrl: 'https://erp-demo.mybahmni.org/', loginScript: '', logoutScript: '', resetScript: '' },
    ];
  }
}
