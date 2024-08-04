import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuiVien } from '../hui-vien.model';
import { HuiService, ResThongKe } from 'app/entities/hui/service/hui.service';
import { filter, map } from 'rxjs/operators';
import { sumBy } from 'lodash';

@Component({
  selector: 'jhi-hui-vien-detail',
  templateUrl: './hui-vien-detail.component.html',
  styleUrls: ['./hui-vien-detail.component.scss'],
})
export class HuiVienDetailComponent implements OnInit {
  huiVien: IHuiVien | null = null;
  thongKe!: ResThongKe;

  constructor(protected activatedRoute: ActivatedRoute, private huiService: HuiService) {
    this.activatedRoute.data.subscribe(({ huiVien }) => (this.huiVien = huiVien));
  }

  ngOnInit(): void {
    console.log(this.huiVien);

    this.huiVien?.chiTietHuis.map((ctHuis: any) => {
      const idHui = ctHuis.hui.id;
      ctHuis.isHuiSong = ctHuis.tienHot !== null;

      this.huiService.find(idHui).subscribe(data => {
        const maxKy = this.timKilonnhat((data?.body as any).chiTietHuis);
        ctHuis.maxKy = maxKy;
        if (ctHuis.tienHot !== null) {
          ctHuis.tienHuiSong = ctHuis.hui.dayHui * ctHuis.maxKy;
        } else {
          ctHuis.tienHuiChet = ctHuis.hui.dayHui * (ctHuis.hui.soPhan - (ctHuis.ky - 1));
        }
      });

      return ctHuis;
    });

    this.getThongKe();
  }

  calculateTotalHuiSong(chiTietHuis: any) {
    return sumBy(chiTietHuis, 'tienHuiSong');
  }

  calculateTotalHuiChet(chiTietHuis: any) {
    return sumBy(chiTietHuis, 'tienHuiChet');
  }

  timKilonnhat(chiTietHuis: any): number {
    return Math.max(...(chiTietHuis || []).map((o: any) => o.ky));
  }

  previousState(): void {
    window.history.back();
  }

  private getThongKe(): void {
    this.huiService
      .getThongKe()
      .pipe(map(({ body }) => body))
      .subscribe(val => (this.thongKe = val));
  }
}
