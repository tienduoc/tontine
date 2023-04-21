import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { sortBy } from 'lodash';
import { MatButton } from '@angular/material/button';
import dayjs from 'dayjs/esm';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';

import { IHui } from '../hui.model';
import { DATE_FORMAT } from 'app/config/input.constants';
import { TinhTienPopupComponent } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { IChiTietHui } from 'app/modules/chi-tiet-hui/chi-tiet-hui.model';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';

export interface DialogData {
  ctHui: any;
  hui: IHui | null;
  chiTietHuiFull: any;
  thamKeuDefault: any;
}

@Component({
  selector: 'jhi-hui-detail',
  templateUrl: './hui-detail.component.html',
  styleUrls: ['./hui-detail.component.scss'],
})
export class HuiDetailComponent implements OnInit {
  hui: IHui | null = null;

  predicate = 'id';
  ascending = true;

  chitietHuis: any;

  ki = 0;

  constructor(private dialog: MatDialog, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
      const newchitietHuis = sortBy([...hui.chiTietHuis], ['huiVien.hoTen', 'ky']);

      this.chitietHuis = newchitietHuis;
    });

    this.timKilonnhat();
  }

  timKilonnhat(): void {
    (this.hui?.chiTietHuis || []).forEach((item: any) => {
      if (item?.ky > this.ki) {
        this.ki = item?.ky;
      }
    });
  }

  openDialogNhapThamKeu(ctHui: IChiTietHui): void {
    console.log({ ctHui });
    const dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
      height: '168px',
      width: '400px',
      data: { ctHui, hui: this.hui },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  navigateToWithComponentValues(): void {}

  public getColorByKi(itemKi: number): string {
    if (itemKi === this.ki) {
      return '#FE0002';
    } else if ( itemKi ) {
      return '#008000';
    } else {
      return '#FFFFFF';
    }
  }

  xemTinhTien(chitietHui: any): void {
    const idChiTietHui = chitietHui?.id;

    if (!idChiTietHui) {
      return;
    }

    this.dialog.open(TinhTienPopupComponent, {
      height: '100%',
      width: '100%',
      maxHeight: '100%',
      maxWidth: '100%',
      data: {
        idChiTietHui,
      },
    });
  }
}

@Component({
  selector: 'input-tham-keu-dialog',
  templateUrl: './input-tham-keu-dialog.html',
})
export class DialogOverviewExampleDialog implements AfterViewInit {
  thamkeuInputValue!: any;
  thamKeuDefault!: number;

  @ViewChild('btnRef') buttonRef!: MatButton;

  constructor(
    private chiTietHuiService: ChiTietHuiService,
    public dialogRef: MatDialogRef<DialogOverviewExampleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private dialog: MatDialog
  ) {
    this.thamKeuDefault = this.data?.chiTietHuiFull?.thamKeu || '';

    const loaiHui = this.data.hui?.loaiHui;

    if (!this.thamKeuDefault && loaiHui && (loaiHui === 'TUAN' || loaiHui === 'NGAY')) {
      const dayHui = this.data.hui?.dayHui || 0;
      const calculateThamkeu = (dayHui * 10) / 100;
      this.thamKeuDefault = calculateThamkeu;
      this.thamkeuInputValue = calculateThamkeu;
    }
  }

  ngAfterViewInit() {
    this.buttonRef.focus();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  inputThamKeu(arg: any) {
    this.thamkeuInputValue = Number(arg.target.value);
  }

  dem = 0;

  save(): void {
    this.dem++;

    if (this.dem > 1) {
      return;
    }

    if (!this.thamkeuInputValue) {
      return;
    }

    if (!this.data?.chiTietHuiFull) {
      const iChiTietHui = {
        huiVien: (this.data.ctHui as any).huiVien,
        id: this.data.ctHui.id,
        thamKeu: this.thamkeuInputValue,
        ngayKhui: dayjs().format(DATE_FORMAT),
        ky: (this.data.ctHui?.ky || 0) + 1,
        hui: {
          dayHui: this.data.hui?.dayHui,
          id: this.data.hui?.id,
          soPhan: this.data.hui?.soPhan,
          tenHui: this.data.hui?.tenHui,
          thamKeu: this.data.hui?.thamKeu,
          loaiHui: this.data.hui?.loaiHui,
        },
      };

      this.chiTietHuiService.update(iChiTietHui as any).subscribe(() => {
        setTimeout(() => {
          const dialogRef = this.dialog.open(TinhTienPopupComponent, {
            height: '100%',
            width: '100%',
            maxHeight: '100%',
            maxWidth: '100%',
            data: {
              idChiTietHui: this.data.ctHui.id,
            },
          });
          dialogRef.afterClosed().subscribe(_ => {
            window.location.reload();
            this.dem = 0;
          });
        }, 200);
      });
    } else {
      const iChiTietHui = {
        ...this.data.chiTietHuiFull,
        thamKeu: this.thamkeuInputValue,
      };

      this.chiTietHuiService.update(iChiTietHui as any).subscribe(() => {
        this.dialogRef.close(true);
        this.dem = 0;
      });
    }
  }
}
