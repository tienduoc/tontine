import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ChiTietHuiComponent } from '../list/chi-tiet-hui.component';
import { ChiTietHuiDetailComponent } from '../detail/chi-tiet-hui-detail.component';
import { ChiTietHuiUpdateComponent } from '../update/chi-tiet-hui-update.component';
import { ChiTietHuiRoutingResolveService } from './chi-tiet-hui-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const chiTietHuiRoute: Routes = [
  {
    path: '',
    component: ChiTietHuiComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ChiTietHuiDetailComponent,
    resolve: {
      chiTietHui: ChiTietHuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ChiTietHuiUpdateComponent,
    resolve: {
      chiTietHui: ChiTietHuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ChiTietHuiUpdateComponent,
    resolve: {
      chiTietHui: ChiTietHuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(chiTietHuiRoute)],
  exports: [RouterModule],
})
export class ChiTietHuiRoutingModule {}
