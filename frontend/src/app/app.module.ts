import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { ElevationDirective } from './directives/elevation/elevation.directive';

// Components
import { AppComponent } from './app.component';
import { NavigationComponent } from './components/navigation/navigation.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { SettingsComponent } from './components/settings/settings.component'
import { AppsComponent } from './components/apps/apps.component';
import { AppDialog } from './dialogs/app/app.dialog';

// Material Components
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialogModule } from '@angular/material/dialog';

const MATERIAL_MODULES = [
  MatIconModule,
  MatToolbarModule,
  MatSidenavModule,
  MatButtonModule,
  MatFormFieldModule,
  MatInputModule,
  MatGridListModule,
  MatCardModule,
  MatTableModule,
  MatMenuModule,
  MatDialogModule
];

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    LoginComponent,
    HomeComponent,
    SettingsComponent,
    ElevationDirective,
    AppsComponent,
    AppDialog
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ...MATERIAL_MODULES
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
