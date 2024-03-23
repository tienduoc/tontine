import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuiVien } from '../hui-vien.model';
import { HuiService, ResThongKe } from 'app/entities/hui/service/hui.service';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'jhi-hui-vien-detail',
  templateUrl: './hui-vien-detail.component.html',
  styleUrls: ['./hui-vien-detail.component.scss'],
})
export class HuiVienDetailComponent implements OnInit {
  huiVien: IHuiVien | null = null;
  thongKe!: ResThongKe;

  constructor(protected activatedRoute: ActivatedRoute, private huiService: HuiService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huiVien }) => {
      this.huiVien = huiVien;
    });
    this.getThongKe();
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
