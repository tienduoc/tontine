import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IChiTietHui } from '../chi-tiet-hui.model';

@Component({
  selector: 'jhi-chi-tiet-hui-detail',
  templateUrl: './chi-tiet-hui-detail.component.html',
})
export class ChiTietHuiDetailComponent implements OnInit {
  chiTietHui: IChiTietHui | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chiTietHui }) => {
      this.chiTietHui = chiTietHui;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
