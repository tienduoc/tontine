import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        { path: '', redirectTo: '/login', pathMatch: 'full' },
        {
          path: 'home',
          loadChildren: () => import('./modules/home/home.module').then(m => m.HomeModule),
        },
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./modules/admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./modules/account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'hui',
          loadChildren: () => import('./modules/hui/hui.module').then(m => m.HuiModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./modules/login/login.module').then(m => m.LoginModule),
        },
        {
          path: 'entitys',
          loadChildren: () => import(`./entities/entity-routing.module`).then(m => m.EntityRoutingModule),
        },
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
