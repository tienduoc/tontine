import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HuiComponent } from './list/hui.component';
import { HuiDetailComponent } from './detail/hui-detail.component';
import { HuiUpdateComponent } from './update/hui-update.component';
import { HuiDeleteDialogComponent } from './delete/hui-delete-dialog.component';
import { HuiRoutingModule } from './route/hui-routing.module';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

@NgModule({
  imports: [SharedModule, HuiRoutingModule, MatCardModule, MatIconModule, MatButtonModule, NgxDatatableModule],
  declarations: [HuiComponent, HuiDetailComponent, HuiUpdateComponent, HuiDeleteDialogComponent],
})
export class HuiModule {}
