import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { AdminGuard } from "../authorization/admin.guard";
import { AuthGuard } from "../authorization/auth.guard";
import { AppsComponent } from "./apps.component";
import { MatTableModule } from '@angular/material/table';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
    declarations: [AppsComponent],
    imports: [
        RouterModule.forChild([{
            path: '',
            pathMatch: 'full',
            component: AppsComponent,
            canActivate: [AuthGuard, AdminGuard]
        }
        ]),
        MatTableModule,
        MatMenuModule,
        MatIconModule,
    ]
})
export class AppsModule { }