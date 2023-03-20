import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChiTietHuiComponent } from './list/chi-tiet-hui.component';
import { ChiTietHuiDetailComponent } from './detail/chi-tiet-hui-detail.component';
import { ChiTietHuiUpdateComponent } from './update/chi-tiet-hui-update.component';
import { ChiTietHuiDeleteDialogComponent } from './delete/chi-tiet-hui-delete-dialog.component';
import { ChiTietHuiRoutingModule } from './route/chi-tiet-hui-routing.module';

@NgModule({
  imports: [SharedModule, ChiTietHuiRoutingModule],
  declarations: [ChiTietHuiComponent, ChiTietHuiDetailComponent, ChiTietHuiUpdateComponent, ChiTietHuiDeleteDialogComponent],
})
export class ChiTietHuiModule {}
