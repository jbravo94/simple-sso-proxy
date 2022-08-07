import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { ElevationDirective } from './directives/elevation/elevation.directive';
import { MonacoEditorModule } from '@materia-ui/ngx-monaco-editor';
import { FormsModule } from '@angular/forms';

// Components
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { LoginComponent } from './authentication/login.component';
import { HomeComponent } from './home/home.component';
import { SettingsComponent } from './settings/settings.component'
import { AppsComponent } from './apps/apps.component';
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
import { ScriptingComponent } from './scripting/scripting.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BearerTokenInterceptor } from './token/bearer-token.interceptor';
import { GridComponent } from './grid/grid.component';

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
    AppDialog,
    ScriptingComponent,
    GridComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MonacoEditorModule,
    ...MATERIAL_MODULES
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BearerTokenInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
