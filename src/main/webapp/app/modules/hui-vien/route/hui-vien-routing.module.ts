import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HuiVienComponent } from '../list/hui-vien.component';
import { HuiVienDetailComponent } from '../detail/hui-vien-detail.component';
import { HuiVienUpdateComponent } from '../update/hui-vien-update.component';
import { HuiVienRoutingResolveService } from './hui-vien-routing-resolve.service';
import { DESC } from 'app/config/navigation.constants';

const huiVienRoute: Routes = [
  {
    path: '',
    component: HuiVienComponent,
    data: {
      defaultSort: 'id,' + DESC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HuiVienDetailComponent,
    resolve: {
      huiVien: HuiVienRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HuiVienUpdateComponent,
    resolve: {
      huiVien: HuiVienRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HuiVienUpdateComponent,
    resolve: {
      huiVien: HuiVienRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(huiVienRoute)],
  exports: [RouterModule],
})
export class HuiVienRoutingModule {}
