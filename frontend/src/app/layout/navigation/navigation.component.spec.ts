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
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from 'src/app/authentication/auth.service';
import { Location } from '@angular/common';

import { NavigationComponent } from './navigation.component';
import { Router } from '@angular/router';
import { routes } from 'src/app/routing/app-routing.module';

describe('NavigationComponent', () => {
  let component: NavigationComponent;
  let fixture: ComponentFixture<NavigationComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let location: Location;
  let router: Router;

  beforeEach(async () => {
    const localAuthServiceSpy = jasmine.createSpyObj('AuthService', ['getLoggedIn', 'isAdmin', 'toggleSidebarState', 'onSidebarClose', 'logout', 'onClickSettings']);

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes(routes), HttpClientTestingModule],
      declarations: [NavigationComponent],
      providers: [{ provide: AuthService, useValue: localAuthServiceSpy }]
    })
      .compileComponents();

    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    location = TestBed.inject(Location);
    router = TestBed.inject(Router);
    router.initialNavigation();

    fixture = TestBed.createComponent(NavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('is logged out view valid', () => {

    authServiceSpy.getLoggedIn.and.returnValue(false);
    authServiceSpy.isAdmin.and.returnValue(false);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    expect(component.isLoggedIn()).toBeFalsy();
    expect(component.isAdmin()).toBeFalsy();

    expect(compiled.querySelector('#sidebar-menu-button')).toBeFalsy();
    expect(compiled.querySelector('#sidebar-settings-button')).toBeFalsy();
    expect(compiled.querySelector('#sidebar-logout-button')).toBeFalsy();
  });

  it('is logged in view valid', () => {

    authServiceSpy.getLoggedIn.and.returnValue(true);
    authServiceSpy.isAdmin.and.returnValue(false);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    expect(component.isLoggedIn()).toBeTruthy();
    expect(component.isAdmin()).toBeFalsy();

    expect(compiled.querySelector('#sidebar-menu-button')).toBeTruthy();
    expect(compiled.querySelector('#sidebar-settings-button')).toBeFalsy();
    expect(compiled.querySelector('#sidebar-logout-button')).toBeTruthy();
  });

  it('is admin view valid', () => {

    authServiceSpy.getLoggedIn.and.returnValue(true);
    authServiceSpy.isAdmin.and.returnValue(true);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    expect(component.isLoggedIn()).toBeTruthy();
    expect(component.isAdmin()).toBeTruthy();

    expect(compiled.querySelector('#sidebar-menu-button')).toBeTruthy();
    expect(compiled.querySelector('#sidebar-settings-button')).toBeTruthy();
    expect(compiled.querySelector('#sidebar-logout-button')).toBeTruthy();
  });

  it('is sidebar toggled', () => {

    expect(component.getIsSidebarOpen()).toBeFalsy();

    component.toggleSidebarState();

    expect(component.getIsSidebarOpen()).toBeTruthy();

    component.onSidebarClose();

    expect(component.getIsSidebarOpen()).toBeFalsy();
  });

  it('is logout clicked', () => {

    authServiceSpy.getLoggedIn.and.returnValue(true);

    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    const menuButton = compiled.querySelector('#sidebar-menu-button');
    const logoutButton = compiled.querySelector('#sidebar-logout-button');

    menuButton.click();
    logoutButton.click();

    expect(authServiceSpy.logout).toHaveBeenCalled();
  });

  it('is settings clicked', fakeAsync(() => {

    authServiceSpy.getLoggedIn.and.returnValue(true);
    authServiceSpy.isAdmin.and.returnValue(true);

    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;

    const menuButton = compiled.querySelector('#sidebar-menu-button');
    const settingsButton = compiled.querySelector('#sidebar-settings-button');

    menuButton.click();
    settingsButton.click();

    tick();

    expect(location.path()).toEqual('/settings');
  }));

});
