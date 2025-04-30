import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ASC } from '../../../config/navigation.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { DsHuiKhuiComponent } from '../list/ds-hui-khui.component';
import { HuiKhuiDetailComponent } from '../hui-khui-detail/hui-khui-detail.component';
import { HuiKhuiRoutingResolveService } from './hui-khui-routing-resolve.service';
import { DsHuiComponent } from '../ds-hui/ds-hui.component';

const dsHuiKhuiRoute: Routes = [
  {
    path: '',
    component: DsHuiKhuiComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id',
    component: DsHuiComponent,
    resolve: {
      chiTietHui: HuiKhuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view/:userId',
    component: HuiKhuiDetailComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dsHuiKhuiRoute)],
  exports: [RouterModule],
})
export class DsHuiKhuiRoutingModule {}
