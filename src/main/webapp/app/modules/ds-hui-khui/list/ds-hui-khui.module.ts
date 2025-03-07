import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatInputModule } from '@angular/material/input';
import { DsHuiKhuiComponent } from './ds-hui-khui.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule, NativeDateModule } from '@angular/material/core';
import { HuiKhuiDetailComponent } from '../hui-khui-detail/hui-khui-detail.component';
import {DsHuiKhuiRoutingModule} from "../route/ds-hui-khui-routing.module";

@NgModule({
  imports: [
    SharedModule,
    DsHuiKhuiRoutingModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    NativeDateModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  declarations: [DsHuiKhuiComponent, HuiKhuiDetailComponent],
})
export class DsHuiKhuiModule {}
