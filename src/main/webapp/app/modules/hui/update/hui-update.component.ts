import { Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { debounceTime, finalize, map, pluck, switchMap, tap } from 'rxjs/operators';
import dayjs from 'dayjs';
import { MatDialog } from '@angular/material/dialog';

import { HuiFormService, HuiFormGroup } from './hui-form.service';
import { IHui } from '../hui.model';
import { HuiService } from '../service/hui.service';
import { LoaiHui } from 'app/entities/enumerations/loai-hui.model';
import { IHuiVien } from 'app/modules/hui-vien/hui-vien.model';
import { HuiVienService } from 'app/modules/hui-vien/service/hui-vien.service';
import { ChiTietHuiService } from 'app/modules/chi-tiet-hui/service/chi-tiet-hui.service';
import { TinhTienPopupComponnet } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { DATE_FORMAT } from 'app/config/input.constants';
import { cloneDeep } from 'lodash';

@Component({
  selector: 'jhi-hui-update',
  templateUrl: './hui-update.component.html',
  styleUrls: ['./hui-update.component.scss'],
})
export class HuiUpdateComponent implements OnInit {
  @ViewChild('searchInput') searchInput!: ElementRef;
  isSaving = false;
  hui: IHui | null = null;
  loaiHuiValues = Object.keys(LoaiHui);
  huiviens: string[] | undefined;
  huivienTable: IHuiVien[] | undefined;
  editForm: HuiFormGroup = this.huiFormService.createHuiFormGroup();
  textError: string = '';

  constructor(
    private router: Router,
    protected huiService: HuiService,
    protected huiFormService: HuiFormService,
    protected activatedRoute: ActivatedRoute,
    private huiVienService: HuiVienService,
    private chiTietHuiService: ChiTietHuiService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getHuiViens();

    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
      if (hui) {
        this.updateForm(hui);
      }
    });

    this.listenHuiviensControl();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.textError = '';

    const hui = this.huiFormService.getHui(this.editForm);
    const newHui = {
      ...this.hui,
      ...hui,
    };

    console.log({ newHui });

    if (hui.id !== null) {
      console.log(hui);
      this.subscribeToSaveResponse(this.huiService.update(newHui as any));
    } else {
      let huiviens: any = [];

      let demHuivien = 0;

      const newHuivienTable = (this.huivienTable || []).filter(({ number }) => (number as number) > 0);

      newHuivienTable.map(huivien => {
        for (let i = 0; i < (huivien.number as number); i++) {
          demHuivien++;
          const { hoTen, id, sdt, thamkeu } = huivien;

          huiviens.push({
            thamKeu: i === 0 && thamkeu ? thamkeu : null,
            ngayKhui: i === 0 && thamkeu ? dayjs().format(DATE_FORMAT) : null,
            ky: i === 0 && thamkeu ? 1 : null,
            huiVien: {
              hoTen,
              id,
              sdt,
            },
          });
        }
      });

      const newHui = {
        ...hui,
        chiTietHuis: [...huiviens],
      };

      if (demHuivien < (hui as any).soPhan) {
        this.textError = '[ Lỗi ] Chưa đủ hụi viên, vui lòng thêm số lượng hụi viên bằng số phần!';
        this.isSaving = false;
        return;
      }

      if (demHuivien > (hui as any).soPhan) {
        this.textError = '[ Lỗi ] Số hụi viên lớn hơn số phần, vui lòng thêm số lượng hụi viên bằng số phần!';

        this.isSaving = false;
        return;
      }

      this.textError = '';
      this.subscribeToSaveResponseForCreateHui(this.huiService.create(newHui as any));
    }
  }

  deleleHuivien(huiVien: any): void {
    if (!huiVien) {
      return;
    }

    const chiTietHui =
      this.hui?.chiTietHuis?.filter(chiTietHui => {
        return chiTietHui?.huiVien?.id === huiVien?.huiVien?.id;
      }) || [];

    if (chiTietHui?.length > 0) {
      this.chiTietHuiService.delete(chiTietHui[0].id).subscribe(() => {
        setTimeout(() => {
          this.router.navigate([`/hui/${this.hui?.id}/view`]);
        }, 300);
      });
    }
  }

  addHuivien(huivien: IHuiVien): void {
    if (!huivien) {
      return;
    }

    this.huivienTable?.map(huivienItem => {
      if (huivienItem.id === huivien.id) {
        return {
          ...huivienItem,
          number: (huivienItem.number as number)++,
        };
      }
      return huivienItem;
    });
  }

  minusHuiVien(huivien: IHuiVien): void {
    if (!huivien) {
      return;
    }

    this.huivienTable?.map(huivienItem => {
      if (huivienItem.id === huivien.id) {
        const number = huivienItem.number !== 0 ? (huivienItem.number as number)-- : 0;

        return {
          ...huivienItem,
          number,
        };
      }
      return huivienItem;
    });
  }

  inputThamKeu(event: any, huivien: any): void {
    this.huivienTable = this.huivienTable?.map(huivienItem => {
      if (huivienItem.id !== huivien.id) {
        return {
          ...huivienItem,
          isDisableInput: true,
        };
      }

      huivienItem.thamkeu = event.target.value;
      return huivienItem;
    });
  }

  resetNhapThamKeu(): void {
    this.searchInput.nativeElement.value = '';
    this.huivienTable = this.huivienTable?.map(huivienItem => {
      huivienItem.isDisableInput = false;
      huivienItem.thamkeu = null;
      return huivienItem;
    });
  }

  protected subscribeToSaveResponseForCreateHui(result: Observable<HttpResponse<IHui>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: data => this.onSaveSuccess(data),
      error: () => this.onSaveError(),
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHui>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.previousState(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(data: any): void {
    const chiTietHuiofHuiVien = data?.body?.chiTietHuis?.filter(({ id, ngayKhui, thamKeu }: any) => !!id && !!ngayKhui && !!thamKeu);
    let idChiTietHui;
    if (chiTietHuiofHuiVien?.length > 0) {
      idChiTietHui = chiTietHuiofHuiVien[0].id;
    }
    if (!idChiTietHui) {
      this.router.navigate([`/hui`]);
      return;
    }

    const dialogRef = this.dialog.open(TinhTienPopupComponnet, {
      height: '90%',
      width: '90%',
      data: {
        idChiTietHui,
      },
    });
    dialogRef.afterClosed().subscribe(_ => {
      this.router.navigate([`/hui/${data?.body?.id}/view`]);
    });
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(hui: IHui): void {
    this.hui = cloneDeep(hui);
    this.huiFormService.resetForm(this.editForm, hui);
  }

  private listenHuiviensControl(): void {
    (this.editForm.get('huiViens') as any).valueChanges
      .pipe(
        debounceTime(300),
        switchMap((val: string) => {
          const iChiTietHui = {
            huiVien: JSON.parse(val),
            id: null,
            hui: {
              ...this.hui,
            },
          };
          return this.chiTietHuiService.create(iChiTietHui as any);
        })
      )
      .subscribe((val: any) => {
        setTimeout(() => {
          this.router.navigate([`/hui/${this.hui?.id}/view`]);
        }, 300);
      });
  }

  private generateHuiViensOptions(huiviens: IHuiVien[] | null): string[] | undefined {
    return huiviens?.map(({ id, hoTen, sdt }) => {
      return JSON.stringify({ id, hoTen, sdt });
    });
  }

  private getHuiViens(page?: number, predicate?: string, ascending?: boolean): void {
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: 999,
    };

    this.huiVienService
      .query(queryObject)
      .pipe(
        pluck('body'),
        tap(data => (this.huivienTable = this.huivienTableCaculate(data as any))),
        map(huiviens => this.generateHuiViensOptions(huiviens))
      )
      .subscribe(huiviens => (this.huiviens = huiviens));
  }

  private huivienTableCaculate(data: IHuiVien[]): any {
    return data?.map(huivien => {
      const newHuiVienItem = {
        ...huivien,
        number: 0,
      };
      return newHuiVienItem;
    });
  }
}
