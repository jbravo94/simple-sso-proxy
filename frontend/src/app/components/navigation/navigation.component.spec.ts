import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from 'src/app/services/auth/auth.service';

import { NavigationComponent } from './navigation.component';

describe('NavigationComponent', () => {
  let component: NavigationComponent;
  let fixture: ComponentFixture<NavigationComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const localAuthServiceSpy = jasmine.createSpyObj('AuthService', ['getLoggedIn', 'isAdmin']);

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule],
      declarations: [NavigationComponent],
      providers: [{ provide: AuthService, useValue: localAuthServiceSpy }]
    })
      .compileComponents();

    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;

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
});
