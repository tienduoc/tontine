import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  exports: [MatCardModule, NgxDatatableModule, MatDialogModule],
})
export class HuiMaterialModule {}

// khoi.hlt TODO: analy this model SharedModule
