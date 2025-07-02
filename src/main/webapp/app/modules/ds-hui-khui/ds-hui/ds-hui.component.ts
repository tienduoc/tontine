import { Component, OnDestroy, OnInit, Renderer2, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IHui } from '../hui.model';
import html2canvas from 'html2canvas';
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

  async captureTableAsImage(): Promise<void> {
    const screenElement = this.screen.nativeElement;

    if (!screenElement) {
      console.error('Không tìm thấy phần tử cần chụp');
      return;
    }

    const tempContainer = document.createElement('div');
    tempContainer.style.position = 'absolute';
    tempContainer.style.left = '-9999px';
    tempContainer.style.top = '0';
    tempContainer.style.backgroundColor = '#ffffff';
    tempContainer.style.padding = '0';
    tempContainer.style.margin = '0';

    try {
      const headerCard = screenElement.querySelector('mat-card');
      const headerClone = headerCard ? headerCard.cloneNode(true) : null;

      const tableContainer = screenElement.querySelector('.table-detail');
      const tableClone = tableContainer ? tableContainer.cloneNode(true) : null;

      if (!headerClone || !tableClone) {
        console.error('Không tìm thấy header hoặc table');
        return;
      }

      tempContainer.appendChild(headerClone);
      tempContainer.appendChild(tableClone);

      document.body.appendChild(tempContainer);

      tempContainer.style.overflow = 'visible';
      tempContainer.style.width = 'auto';
      tempContainer.style.maxWidth = 'none';
      tempContainer.style.height = 'auto';

      const tableCloneElement = tempContainer.querySelector('.table-detail') as HTMLElement;
      if (tableCloneElement) {
        tableCloneElement.style.overflow = 'visible';
        tableCloneElement.style.width = 'auto';
        tableCloneElement.style.maxWidth = 'none';
        tableCloneElement.style.height = 'auto';
      }

      await new Promise(resolve => setTimeout(resolve, 100));

      const pixelRatio = window.devicePixelRatio || 1;
      const scaleFactor = Math.max(pixelRatio, 2);

      const canvas = await html2canvas(tempContainer, {
        useCORS: true,
        allowTaint: true,
        scale: scaleFactor,
        width: tempContainer.scrollWidth,
        height: tempContainer.scrollHeight,
        scrollX: 0,
        scrollY: 0,
        windowWidth: tempContainer.scrollWidth,
        windowHeight: tempContainer.scrollHeight,
        backgroundColor: '#ffffff',
        logging: false,
        imageTimeout: 15000,
        removeContainer: true,
        onclone(clonedDoc) {
          const allElements = clonedDoc.querySelectorAll('*');
          allElements.forEach((el: any) => {
            if (el.style) {
              el.style.overflow = 'visible';
              el.style.overflowX = 'visible';
              el.style.overflowY = 'visible';
              el.style.fontSmooth = 'always';
              el.style.webkitFontSmoothing = 'antialiased';
            }
          });
        }
      });

      const imgData = canvas.toDataURL('image/png', 1.0);
      const link = document.createElement('a');
      link.download = `hui-table-${this.selectedDate || 'export'}-hq.png`;
      link.href = imgData;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

    } catch (error) {
      console.error('Error capturing image:', error);
    } finally {
      if (tempContainer && document.body.contains(tempContainer)) {
        document.body.removeChild(tempContainer);
      }
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

  hasAnySoTienHot(): boolean {
    return this.chiTietHuis.some(item => item.soTienHot !== null && item.soTienHot !== undefined && item.soTienHot > 0);
  }
}
