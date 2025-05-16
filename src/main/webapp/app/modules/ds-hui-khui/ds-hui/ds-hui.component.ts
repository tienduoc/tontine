import { Component, OnDestroy, OnInit, Renderer2, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IHui } from '../hui.model';
import { NgxCaptureService } from 'ngx-capture';
import { DsHuiKhuiService } from '../service/ds-hui-khui.service';

@Component({
  selector: 'jhi-ds-hui',
  templateUrl: './ds-hui.component.html',
  styleUrls: ['./ds-hui.component.scss'],
})
export class DsHuiComponent implements OnInit, OnDestroy {
  hui: IHui | null = null;
  selectedDate: string;
  huiVien: string | null = null;
  ngay: string | null = null;

  predicate = 'id';
  ascending = true;

  @ViewChild('screen') screen!: ElementRef;

  chiTietHuis: Array<{
    huiId: number;
    dayHui?: string | null;
    soTien?: number | null;
    soChan?: number | null;
    soKyDong?: number | null;
    soTienBoTham?: number | null;
    chet?: boolean | null;
    song?: boolean | null;
    soTienDong?: number | null;
    soTienHot?: number | null;
    tongBill?: number | null;
  }> = [];

  constructor(
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    private renderer: Renderer2,
    private captureService: NgxCaptureService,
    private dsHuiKhuiService: DsHuiKhuiService
  ) {
    this.selectedDate = this.dsHuiKhuiService.getSelectedDate();
  }

  ngOnDestroy(): void {
    const cardGlobal = document.querySelector('.card-global');
    if (cardGlobal) {
      this.renderer.setStyle(cardGlobal, 'display', 'block');
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chiTietHui }) => {
      this.hui = chiTietHui;

      // Handle the new response structure
      if (chiTietHui) {
        // Extract huiVien and ngay fields
        this.huiVien = chiTietHui.huiVien || null;
        this.ngay = chiTietHui.ngay || null;

        // If ngay is provided, use it instead of selectedDate
        if (this.ngay) {
          this.selectedDate = this.ngay;
        }

        // Extract huiKhuiResponseList if available, otherwise use chiTietHui directly
        this.chiTietHuis = chiTietHui.huiKhuiResponseList || chiTietHui;
      }
    });

    const cardGlobal = document.querySelector('.card-global');
    if (cardGlobal) {
      this.renderer.setStyle(cardGlobal, 'display', 'none');
    }
  }

  goBack(): void {
    this.router.navigate(['../'], { relativeTo: this.activatedRoute });
  }

  captureTableAsImage(): void {
    const tableContainer = this.screen.nativeElement.querySelector('.table-detail');
    if (tableContainer) {
      // Save original styles
      const originalOverflow = tableContainer.style.overflow;
      const originalWidth = tableContainer.style.width;
      const originalMaxWidth = tableContainer.style.maxWidth;

      // Get the table element
      const tableElement = tableContainer.querySelector('table');
      const originalTableWidth = tableElement.style.width;

      // Temporarily modify styles to ensure full table is captured
      tableContainer.style.overflow = 'visible';
      tableContainer.style.width = 'auto';
      tableContainer.style.maxWidth = 'none';
      tableElement.style.width = 'auto';

      this.captureService.getImage(tableContainer, true).subscribe(img => {
        // Restore original styles
        tableContainer.style.overflow = originalOverflow;
        tableContainer.style.width = originalWidth;
        tableContainer.style.maxWidth = originalMaxWidth;
        tableElement.style.width = originalTableWidth;

        // Create a link element to download the image
        const link = document.createElement('a');
        link.download = 'table-image.png';
        link.href = img;
        link.click();
      });
    }
  }

  getTotalSoTienDong(): number {
    return this.chiTietHuis.reduce((sum, item) => sum + (item.soTienDong ?? 0), 0);
  }

  getTotalSoTienHot(): number {
    return this.chiTietHuis.reduce((sum, item) => sum + (item.soTienHot ?? 0), 0);
  }

  getTong(): number {
    return this.getTotalSoTienHot() - this.getTotalSoTienDong();
  }
}
