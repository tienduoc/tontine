import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HomeComponent } from './home.component';

const routes: Routes = [
    {
      path: '',
      component: HomeComponent,
      data: {
        pageTitle: 'Quản lý hụi!',
      },
      canActivate: [UserRouteAccessService],
    }
]

@NgModule({
    imports: [
      RouterModule.forChild(routes)
    ],
    exports: [RouterModule]
})
export class HomeRoutingModule { }