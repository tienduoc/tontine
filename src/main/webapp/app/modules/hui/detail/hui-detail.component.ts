import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHui } from '../hui.model';

@Component({
  selector: 'jhi-hui-detail',
  templateUrl: './hui-detail.component.html',
})
export class HuiDetailComponent implements OnInit {
  hui: IHui | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
