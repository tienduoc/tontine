import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ASC } from '../../../config/navigation.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';
import { DsHuiKhuiComponent } from '../list/ds-hui-khui.component';
import { HuiKhuiDetailComponent } from '../hui-khui-detail/hui-khui-detail.component';
import { HuiKhuiRoutingResolveService } from './hui-khui-routing-resolve.service';

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
    path: ':id/view',
    component: HuiKhuiDetailComponent,
    resolve: {
      chiTietHui: HuiKhuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dsHuiKhuiRoute)],
  exports: [RouterModule],
})
export class DsHuiKhuiRoutingModule {}
