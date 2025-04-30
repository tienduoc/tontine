import { AfterViewInit, Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { sortBy } from 'lodash';
import { MatButton } from '@angular/material/button';
import dayjs from 'dayjs/esm';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';

import { IHui } from '../hui.model';
import { DATE_FORMAT } from 'app/config/input.constants';
import { TinhTienPopupComponent } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { IChiTietHui } from 'app/modules/chi-tiet-hui/chi-tiet-hui.model';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';
import { NgxCaptureService } from 'ngx-capture';
import { finalize, tap } from 'rxjs';

export interface DialogData {
  ctHui: any;
  hui: IHui | null;
  chiTietHuiFull: any;
  thamKeuDefault: any;
}

@Component({
  selector: 'jhi-ds-tv',
  templateUrl: './ds-tv.component.html',
  styleUrls: ['./ds-tv.component.scss'],
})
export class DstvComponent implements OnInit, OnDestroy {
  @ViewChild('screen', { static: true }) screen: any;

  hui: IHui | null = null;

  predicate = 'id';
  ascending = true;

  chiTietHuis: any;

  ki = 0;

  constructor(
    private dialog: MatDialog,
    protected activatedRoute: ActivatedRoute,
    private captureService: NgxCaptureService,
    private router: Router
  ) {}

  ngOnDestroy(): void {
    (document.querySelector('.card-global') as any).style.display = 'block';
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chiTietHui }) => {
      this.hui = chiTietHui;
      this.chiTietHuis = chiTietHui;
    });

    setTimeout(() => {
      (document.querySelector('.card-global') as any).style.display = 'none';
    }, 0);
  }

  goBack(): void {
    this.router.navigate(['../'], { relativeTo: this.activatedRoute });
  }
}
