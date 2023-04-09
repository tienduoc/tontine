import { Component, Inject } from '@angular/core';
import { pluck } from 'rxjs';

import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TinhTienService } from './service/tinh-tien-popup.service';
import { DialogOverviewExampleDialog } from 'app/modules/hui/detail/hui-detail.component';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';

@Component({
  selector: 'jhi-tinh-tien-popup',
  templateUrl: './tinh-tien-popup.component.html',
  styleUrls: ['./tinh-tien-popup.component.scss'],
})
export class TinhTienPopupComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { idChiTietHui: number },
    private tinhTienService: TinhTienService,
    private dialog: MatDialog,
    private chiTietHuiService: ChiTietHuiService
  ) {}
  tinhtien: any;
  chiTietHuiFull: any;

  ngOnInit(): void {
    this.tintienAPI();
    this.getTtChitietHui();
  }

  openDialogNhapThamKeu(): void {
    const dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
      height: '168px',
      width: '400px',
      data: { chiTietHuiFull: this.chiTietHuiFull },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog nhap tham keu was closed');
      setTimeout(() => {
        this.tintienAPI();
        this.getTtChitietHui();
      }, 200);
    });
  }

  close() {
    window.location.reload();
  }

  getTtChitietHui(): void {
    this.chiTietHuiService
      .find(this.data.idChiTietHui)
      .pipe(pluck('body'))
      .subscribe(data => {
        this.chiTietHuiFull = data;
      });
  }

  tintienAPI(): void {
    this.tinhTienService
      .find(this.data.idChiTietHui)
      .pipe(pluck('body'))
      .subscribe(data => {
        this.tinhtien = data;
      });
  }
}
