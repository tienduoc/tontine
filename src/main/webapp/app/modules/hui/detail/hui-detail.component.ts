import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHui } from '../hui.model';

@Component({
  selector: 'jhi-hui-detail',
  templateUrl: './hui-detail.component.html',
})
export class HuiDetailComponent implements OnInit {
  hui: IHui | null = null;

  huis = [
    { id: 11, name: 'cau ca', sdt: 1232323 },
    { id: 11, name: 'cau ca', sdt: 1232323 },
  ];

  predicate = 'id';
  ascending = true;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
    });
  }

  previousState(): void {
    window.history.back();
  }

  navigateToWithComponentValues(): void {}
}
