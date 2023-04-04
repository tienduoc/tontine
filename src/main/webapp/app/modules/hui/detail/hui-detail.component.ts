import { Component, Inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';

import { IHui } from '../hui.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TinhTienPopupComponnet } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { sortBy } from 'lodash';
import { IChiTietHui } from 'app/modules/chi-tiet-hui/chi-tiet-hui.model';

export interface DialogData {
  ctHui: any;
  hui: IHui | null;
}

@Component({
  selector: 'jhi-hui-detail',
  templateUrl: './hui-detail.component.html',
  styleUrls: ['./hui.component.scss'],
})
export class HuiDetailComponent implements OnInit {
  hui: IHui | null = null;

  predicate = 'id';
  ascending = true;

  chitietHuis: any;

  constructor(
    private dialog: MatDialog,
    protected activatedRoute: ActivatedRoute,
    private chiTietHuiService: ChiTietHuiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
      const newchitietHuis = sortBy([...hui.chiTietHuis], ['ky']);
      newchitietHuis;

      const xx = newchitietHuis.map(data => {
        if (data.ky === null) {
          return {
            ...data,
            ky: 0,
          };
        }
        return data;
      });

      this.chitietHuis = sortBy([...xx], ['ky']).map(data => {
        if (data.ky === 0) {
          return {
            ...data,
            ky: null,
          };
        }
        return data;
      });
    });
  }

  previousState(): void {
    window.history.back();
  }

  openDialogNhapThamKeu(ctHui: IChiTietHui): void {
    const dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
      height: '200px',
      width: '400px',
      data: { ctHui, hui: this.hui },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      // this.animal = result;
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
      return '#00A8FF';
    } else {
      return '#ff77a5';
    }
  }

  xemTinhTien(chitietHui: any): void {
    const idChiTietHui = chitietHui?.id;

    if (!idChiTietHui) {
      return;
    }

    this.dialog.open(TinhTienPopupComponnet, {
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
  selector: 'dialog-overview-example-dialog',
  templateUrl: './dialog-overview-example-dialog.html',
})
export class DialogOverviewExampleDialog {
  thamkeuInputValue!: any;

  constructor(
    private chiTietHuiService: ChiTietHuiService,
    public dialogRef: MatDialogRef<DialogOverviewExampleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private dialog: MatDialog
  ) {}

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
        const dialogRef = this.dialog.open(TinhTienPopupComponnet, {
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
  }
}
