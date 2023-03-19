import { NgModule } from '@angular/core';

import { DialogContentExampleDialog, HuiComponent } from './list/hui.component';
import { HuiDetailComponent } from './detail/hui-detail.component';
import { HuiUpdateComponent } from './update/hui-update.component';
import { HuiDeleteDialogComponent } from './delete/hui-delete-dialog.component';
import { HuiRoutingModule } from './route/hui-routing.module';
import { HuiMaterialModule } from './hui-material.module';

@NgModule({
  imports: [HuiRoutingModule, HuiMaterialModule],
  declarations: [HuiComponent, HuiDetailComponent, HuiUpdateComponent, HuiDeleteDialogComponent, DialogContentExampleDialog],
})
export class HuiModule {}

// khoi.hlt TODO: analy this model SharedModule
