import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HuiComponent } from '../list/hui.component';
import { HuiDetailComponent } from '../detail/hui-detail.component';
import { HuiUpdateComponent } from '../update/hui-update.component';
import { HuiRoutingResolveService } from './hui-routing-resolve.service';
import { ASC, DESC } from 'app/config/navigation.constants';

const huiRoute: Routes = [
  {
    path: '',
    component: HuiComponent,
    data: {
      defaultSort: 'id,' + DESC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HuiDetailComponent,
    resolve: {
      hui: HuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HuiUpdateComponent,
    resolve: {
      hui: HuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HuiUpdateComponent,
    resolve: {
      hui: HuiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(huiRoute)],
  exports: [RouterModule],
})
export class HuiRoutingModule {}
