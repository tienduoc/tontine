import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HuiComponent } from './list/hui.component';
import { HuiDetailComponent } from './detail/hui-detail.component';
import { HuiUpdateComponent } from './update/hui-update.component';
import { HuiDeleteDialogComponent } from './delete/hui-delete-dialog.component';
import { HuiRoutingModule } from './route/hui-routing.module';

@NgModule({
  imports: [SharedModule, HuiRoutingModule],
  declarations: [HuiComponent, HuiDetailComponent, HuiUpdateComponent, HuiDeleteDialogComponent],
})
export class HuiModule {}
