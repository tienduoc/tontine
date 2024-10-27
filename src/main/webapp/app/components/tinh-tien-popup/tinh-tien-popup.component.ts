import { Component, Inject } from '@angular/core';
import { pluck } from 'rxjs';

import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { TinhTienService } from './service/tinh-tien-popup.service';
import { DialogOverviewExampleDialog } from 'app/modules/hui/detail/hui-detail.component';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';
import { Router } from '@angular/router';

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
    private chiTietHuiService: ChiTietHuiService,
    private router: Router
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
      setTimeout(() => {
        this.tintienAPI();
        this.getTtChitietHui();
      }, 200);
    });
  }

  close() {
    if (this.router.url === '/hui/new') {
      this.router.navigate([`/hui/${this.chiTietHuiFull?.hui?.id}/view`]);
      setTimeout(() => {
        window.location.reload();
      }, 100);
    } else {
      window.location.reload();
    }
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
