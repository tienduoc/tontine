import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HuiVienComponent } from './list/hui-vien.component';
import { HuiVienDetailComponent } from './detail/hui-vien-detail.component';
import { HuiVienUpdateComponent } from './update/hui-vien-update.component';
import { HuiVienDeleteDialogComponent } from './delete/hui-vien-delete-dialog.component';
import { HuiVienRoutingModule } from './route/hui-vien-routing.module';

@NgModule({
  imports: [SharedModule, HuiVienRoutingModule],
  declarations: [HuiVienComponent, HuiVienDetailComponent, HuiVienUpdateComponent, HuiVienDeleteDialogComponent],
})
export class HuiVienModule {}
