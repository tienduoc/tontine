import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';

import { IHui } from '../hui.model';
import { IChiTietHui } from '../../chi-tiet-hui/chi-tiet-hui.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-hui-detail',
  templateUrl: './hui-detail.component.html',
})
export class HuiDetailComponent implements OnInit {
  hui: IHui | null = null;

  predicate = 'id';
  ascending = true;

  constructor(protected activatedRoute: ActivatedRoute, private chiTietHuiService: ChiTietHuiService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
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
        this.previousState();
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

    let x = '';

    if (dateToCheck.getTime() === currentDate.getTime()) {
      x = '#dc3545';
    } else if (dateToCheck < currentDate) {
      x = '#45fe01';
    } else {
      x = 'red';
    }

    return x;
  }
}
