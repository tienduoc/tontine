import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        { path: '', redirectTo: '/hui', pathMatch: 'full' },
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
          path: 'hui-vien',
          data: { pageTitle: 'HuiViens' },
          loadChildren: () => import('./modules/hui-vien/hui-vien.module').then(m => m.HuiVienModule),
        },
        {
          path: 'dshothomnay',
          loadChildren: () => import('./modules/dshothomnay/dshothomnay.module').then(m => m.DsHotHomNayModule),
        },
        {
          path: 'chi-tiet-hui',
          data: { pageTitle: 'ChiTietHuis' },
          loadChildren: () => import('./modules/chi-tiet-hui/chi-tiet-hui.module').then(m => m.ChiTietHuiModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./modules/login/login.module').then(m => m.LoginModule),
        },
        ...errorRoute,
      ],
      { onSameUrlNavigation: 'reload' }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
