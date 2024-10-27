import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatInputModule } from '@angular/material/input';
import { RouterModule } from '@angular/router';
import { DsHotHomNayComponent } from './dshothomnay.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule, NativeDateModule } from '@angular/material/core';
@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: '',
        component: DsHotHomNayComponent,
      },
    ]),
  ],
  exports: [RouterModule],
})
class DsHotHomNayRouting {}

@NgModule({
  imports: [
    SharedModule,
    DsHotHomNayRouting,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    NativeDateModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  declarations: [DsHotHomNayComponent],
})
export class DsHotHomNayModule {}
