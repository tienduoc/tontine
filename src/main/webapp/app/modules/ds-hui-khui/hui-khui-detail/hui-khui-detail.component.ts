import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { sortBy } from 'lodash';
import { MatDialog } from '@angular/material/dialog';

import { TinhTienPopupComponent } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { IHui } from '../../hui/hui.model';

@Component({
  selector: 'jhi-hui-khui-detail',
  templateUrl: './hui-khui-detail.component.html',
  styleUrls: ['./hui-khui-detail.component.scss'],
})
export class HuiKhuiDetailComponent implements OnInit, OnDestroy {
  @ViewChild('screen', { static: true }) screen: any;

  hui: IHui | null = null;

  predicate = 'id';

  ascending = true;

  chiTietHuis: any;

  ki = 0;

  constructor(private dialog: MatDialog, protected activatedRoute: ActivatedRoute) {}

  ngOnDestroy(): void {
    (document.querySelector('.card-global') as any).style.display = 'block';
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
      this.chiTietHuis = sortBy([...hui.chiTietHuis], ['huiVien.hoTen', 'ky']);
    });

    this.getKiLonNhat();

    setTimeout(() => {
      (document.querySelector('.card-global') as any).style.display = 'none';
    }, 0);
  }

  getKiLonNhat(): void {
    (this.hui?.chiTietHuis || []).forEach((item: any) => {
      if (item?.ky > this.ki) {
        this.ki = item?.ky;
      }
    });
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

  xemTinhTien(chiTietHui: any): void {
    const idChiTietHui = chiTietHui?.id;

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
