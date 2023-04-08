import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHuiVien } from '../hui-vien.model';

@Component({
  selector: 'jhi-hui-vien-detail',
  templateUrl: './hui-vien-detail.component.html',
})
export class HuiVienDetailComponent implements OnInit {
  huiVien: IHuiVien | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huiVien }) => {
      this.huiVien = huiVien;
    });
  }

  // previousState(): void {
  //   window.history.back();
  // }
}
