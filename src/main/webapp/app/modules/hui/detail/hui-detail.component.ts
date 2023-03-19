import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BaseComponent } from 'app/components/base-component/base.component';
import { takeUntil } from 'rxjs';

import { IHui } from '../hui.model';

@Component({
  selector: 'jhi-hui-detail',
  templateUrl: './hui-detail.component.html',
})
export class HuiDetailComponent extends BaseComponent implements OnInit {
  hui: IHui | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {
    super();
  }

  ngOnInit(): void {
    this.activatedRoute.data.pipe(
      takeUntil( this.destroyed$ )
    ).subscribe(({ hui }) => {
      this.hui = hui;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
