import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';

import { IHui } from '../hui.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';
import { MatDialog } from '@angular/material/dialog';
import { TinhTienPopupComponnet } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { sortBy } from 'lodash';

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

  navigateToWithComponentValues(): void {}

  saveAddThamKeu(hui: any, thamkeuInput: any): void {
    const iChiTietHui = {
      huiVien: (hui as any).huiVien,
      id: hui.id,
      thamKeu: thamkeuInput.value,
      ngayKhui: dayjs().format(DATE_FORMAT),
      ky: (hui?.ky || 0) + 1,
      hui: {
        dayHui: this.hui?.dayHui,
        id: this.hui?.id,
        soPhan: this.hui?.soPhan,
        tenHui: this.hui?.tenHui,
        thamKeu: this.hui?.thamKeu,
        loaiHui: this.hui?.loaiHui,
      },
    };

    this.chiTietHuiService.update(iChiTietHui as any).subscribe(() => {
      setTimeout(() => {
        window.location.reload();
      }, 200);
    });
  }

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
      return '#28a745';
    } else {
      return '#dc3545';
    }
  }

  xemTinhTien(chitietHui: any): void {
    const idChiTietHui = chitietHui?.id;

    if (!idChiTietHui) {
      return;
    }

    this.dialog.open(TinhTienPopupComponnet, {
      height: '90%',
      width: '90%',
      data: {
        idChiTietHui,
      },
    });
  }
}
