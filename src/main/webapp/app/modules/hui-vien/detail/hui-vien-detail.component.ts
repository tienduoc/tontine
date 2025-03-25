import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuiVien } from '../hui-vien.model';

@Component({
  selector: 'jhi-hui-vien-detail',
  templateUrl: './hui-vien-detail.component.html',
  styleUrls: ['./hui-vien-detail.component.scss'],
})
export class HuiVienDetailComponent implements OnInit {
  huiVien: IHuiVien | null = null;
  thongKe!: number;

  constructor(protected activatedRoute: ActivatedRoute) {
    this.activatedRoute.data.subscribe(({ huiVien }) => (this.huiVien = huiVien));
  }

  ngOnInit(): void {
    // this.getThongKe();
  }

  previousState(): void {
    window.history.back();
  }
}
