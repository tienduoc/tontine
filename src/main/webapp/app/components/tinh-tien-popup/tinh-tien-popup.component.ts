import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { pluck } from 'rxjs';

import { TinhTienService } from './service/tinh-tien-popup.service';

@Component({
  selector: 'jhi-tinh-tien-popup',
  templateUrl: './tinh-tien-popup.component.html',
  styleUrls: ['./tinh-tien-popup.component.scss'],
})
export class TinhTienPopupComponnet {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { idChiTietHui: number }, private tinhTienService: TinhTienService) {}
  tinhtien: any;

  ngOnInit(): void {
    this.tinhTienService
      .find(this.data.idChiTietHui)
      .pipe(pluck('body'))
      .subscribe(data => {
        this.tinhtien = data;
      });
  }
}
