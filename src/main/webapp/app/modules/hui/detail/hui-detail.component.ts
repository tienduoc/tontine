import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';

import { IHui } from '../hui.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TinhTienPopupComponent } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { sortBy } from 'lodash';
import { IChiTietHui } from 'app/modules/chi-tiet-hui/chi-tiet-hui.model';
import { MatButton } from '@angular/material/button';

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
      // const xx = newchitietHuis.map(data => {
      //   if (data.ky === null) {
      //     return {
      //       ...data,
      //       ky: 0,
      //     };
      //   }
      //   return data;
      // });

      // this.chitietHuis = sortBy([...xx], ['ky']).map(data => {
      //   if (data.ky === 0) {
      //     return {
      //       ...data,
      //       ky: null,
      //     };
      //   }
      //   return data;
      // });

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

  public checkDate(dateToCheckStr: string): string {
    if (!dateToCheckStr) {
      return 'while';
    }
    const dateToCheck = new Date(dateToCheckStr);
    dateToCheck.setHours(0, 0, 0, 0);
    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);

    if (dateToCheck.getTime() === currentDate.getTime()) {
    }
    if (dateToCheck < currentDate) {
      return '#008000';
    } else {
      return '#FE0002';
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
  }

  ngAfterViewInit() {
    this.buttonRef.focus();
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  inputThamKeu(arg: any) {
    this.thamkeuInputValue = arg.target.value;
  }

  save(): void {
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
          });
        }, 200);
      });
    } else {
      const iChiTietHui = {
        ...this.data.chiTietHuiFull,
        thamKeu: this.thamkeuInputValue,
        // ngayKhui: dayjs().format(DATE_FORMAT),
        // ky: (this.data.ctHui?.ky || 0) + 1,
      };

      this.chiTietHuiService.update(iChiTietHui as any).subscribe(() => {
        this.dialogRef.close(true);
      });
    }
  }
}
