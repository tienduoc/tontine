import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HuiComponent } from './list/hui.component';
import { DialogOverviewExampleDialog, HuiDetailComponent } from './detail/hui-detail.component';
import { HuiUpdateComponent } from './update/hui-update.component';
import { HuiDeleteDialogComponent } from './delete/hui-delete-dialog.component';
import { HuiRoutingModule } from './route/hui-routing.module';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { NgxMatSelectSearchModule } from 'ngx-mat-select-search';

@NgModule({
  imports: [
    MatFormFieldModule,
    SharedModule,
    HuiRoutingModule,
    MatSelectModule,
    MatChipsModule,
    MatButtonModule,
    MatIconModule,
    FormsModule,
    NgxMatSelectSearchModule,
  ],
  declarations: [HuiComponent, HuiDetailComponent, HuiUpdateComponent, HuiDeleteDialogComponent, DialogOverviewExampleDialog],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class HuiModule {}
