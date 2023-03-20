import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  exports: [MatCardModule, NgxDatatableModule, MatDialogModule, MatIconModule, MatButtonModule],
})
export class HuiMaterialModule {}

// khoi.hlt TODO: analy this model SharedModule
