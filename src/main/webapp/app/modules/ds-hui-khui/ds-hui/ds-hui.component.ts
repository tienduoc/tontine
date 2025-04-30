import { Component, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IHui } from '../hui.model';

@Component({
  selector: 'jhi-ds-hui',
  templateUrl: './ds-hui.component.html',
  styleUrls: ['./ds-hui.component.scss'],
})
export class DsHuiComponent implements OnInit, OnDestroy {
  hui: IHui | null = null;

  predicate = 'id';
  ascending = true;

  chiTietHuis: Array<{
    huiId: number;
    dayHui?: number | null;
    soTien?: number | null;
    soChan?: number | null;
    soKyDong?: number | null;
    soTienBoTham?: number | null;
    chet?: boolean | null;
    song?: boolean | null;
    soTienDong?: number | null;
  }> = [];

  constructor(
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    private renderer: Renderer2
  ) {}

  ngOnDestroy(): void {
    // Restore card-global visibility when component is destroyed
    const cardGlobal = document.querySelector('.card-global');
    if (cardGlobal) {
      this.renderer.setStyle(cardGlobal, 'display', 'block');
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chiTietHui }) => {
      this.hui = chiTietHui;
      this.chiTietHuis = chiTietHui;
    });

    // Hide card-global when component is initialized
    const cardGlobal = document.querySelector('.card-global');
    if (cardGlobal) {
      this.renderer.setStyle(cardGlobal, 'display', 'none');
    }
  }

  goBack(): void {
    this.router.navigate(['../'], { relativeTo: this.activatedRoute });
  }
}