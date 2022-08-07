import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppsComponent } from '../apps/apps.component';
import { HomeComponent } from '../layout/home/home.component';
import { LoginComponent } from '../authentication/login.component';
import { SettingsComponent } from '../layout/settings/settings.component';
import { AdminGuard } from '../authorization/admin.guard';
import { AuthGuard } from '../authorization/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard, AdminGuard] },
  {
    path: 'settings/apps',
    loadChildren: () => import('../apps/apps.module').then(m => m.AppsModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
