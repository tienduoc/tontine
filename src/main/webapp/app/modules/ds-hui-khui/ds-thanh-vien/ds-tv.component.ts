import { AfterViewInit, Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
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
import { NgxCaptureService } from 'ngx-capture';
import { finalize, tap } from 'rxjs';

export interface DialogData {
  ctHui: any;
  hui: IHui | null;
  chiTietHuiFull: any;
  thamKeuDefault: any;
}

@Component({
  selector: 'jhi-ds-tv',
  templateUrl: './ds-tv.component.html',
  styleUrls: ['./ds-tv.component.scss'],
})
export class DstvComponent implements OnInit, OnDestroy {
  @ViewChild('screen', { static: true }) screen: any;

  hui: IHui | null = null;

  predicate = 'id';
  ascending = true;

  chitietHuis: any;

  ki = 0;

  constructor(
    private dialog: MatDialog,
    protected activatedRoute: ActivatedRoute,
    private captureService: NgxCaptureService,
    private router: Router
  ) {}

  ngOnDestroy(): void {
    (document.querySelector('.card-global') as any).style.display = 'block';
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chiTietHui }) => {
      this.hui = chiTietHui;
      const newchitietHuis = sortBy([...chiTietHui?.chiTietHuis], ['huiVien.hoTen', 'ky']);

      this.chitietHuis = newchitietHuis;
    });

    this.timKilonnhat();

    setTimeout(() => {
      (document.querySelector('.card-global') as any).style.display = 'none';
    }, 0);
  }

  capture() {
    (document.querySelector('.back') as any).style.display = 'none';
    (document.querySelector('.edit') as any).style.display = 'none';
    const matIconList = document.querySelectorAll('.mat-icon-button');
    if (matIconList != null) {
      matIconList.forEach(function (e) {
        (e as any).style.display = 'none';
      });
    }

    this.captureService
      .getImage(this.screen.nativeElement, true)
      .pipe(
        tap(img => {
          const link = document.createElement('a');

          document.body.appendChild(link);

          link.setAttribute('href', img);

          const strWithDiacritics = this.hui?.tenHui as string;
          const strWithoutDiacritics = strWithDiacritics
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace(/\s+/g, '')
            .trim();

          const ngayTao = this.hui?.ngayTao?.format('DD-MM-YYYY');

          link.setAttribute('download', `${strWithoutDiacritics}_${ngayTao}`);

          link.click();
        }),
        finalize(() => {
          (document.querySelector('.back') as any).style.display = 'block';
          (document.querySelector('.edit') as any).style.display = 'block';
          const matIconList = document.querySelectorAll('.mat-icon-button');
          if (matIconList != null) {
            matIconList.forEach(function (e) {
              (e as any).style.display = 'inline-block';
            });
          }
        })
      )
      .subscribe();
  }

  timKilonnhat(): void {
    (this.hui?.chiTietHuis || []).forEach((item: any) => {
      if (item?.ky > this.ki) {
        this.ki = item?.ky;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['../'], { relativeTo: this.activatedRoute });
  }

  openDialogNhapThamKeu(ctHui: IChiTietHui): void {
    const dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
      height: '168px',
      width: '400px',
      data: { ctHui, hui: this.hui },
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog thamkeu was closed');
    });
  }

  openDialogChangeNickName(ctHui: IChiTietHui): void {
    this.router.navigate(['view', ctHui?.huiVien?.id], { relativeTo: this.activatedRoute });
  }

  navigateToWithComponentValues(): void {}

  public getColorByKi(itemKi: number): string {
    if (itemKi === this.ki) {
      return '#FE0002';
    } else if (itemKi) {
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
  @ViewChild('inputThamkeu', { static: true }) inputThamkeu!: ElementRef;

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

  xoaThamKeu() {
    this.thamKeuDefault = -1;
    this.thamkeuInputValue = -1;
    this.save(true);
    window.location.reload();
  }

  ngAfterViewInit() {
    this.buttonRef?.focus();
  }

  onNoClick(): void {
    this.dialogRef?.close();
  }

  inputThamKeu(arg: any) {
    this.thamkeuInputValue = Number(arg.target.value);
  }

  dem = 0;

  save = (closePopup?: boolean) => {
    this.dem++;

    if (this.dem > 1) {
      return;
    }

    if (this.thamkeuInputValue === null || this.thamkeuInputValue === undefined) {
      return;
    }

    if (!this.data?.chiTietHuiFull) {
      const iChiTietHui = {
        huiVien: (this.data.ctHui as any).huiVien,
        id: this.data.ctHui.id,
        thamKeu: this.thamkeuInputValue,
        ngayKhui: dayjs().format(DATE_FORMAT),
        ky: (this.data.ctHui?.ky || 0) + 1,
        nickNameHuiVien: this.data.ctHui?.nickNameHuiVien,
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
  };
}
