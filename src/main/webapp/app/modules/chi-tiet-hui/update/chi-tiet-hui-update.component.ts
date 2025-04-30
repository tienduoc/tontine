import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ChiTietHuiFormService, ChiTietHuiFormGroup } from './chi-tiet-hui-form.service';
import { IChiTietHui } from '../chi-tiet-hui.model';
import { ChiTietHuiService } from '../service/chi-tiet-hui.service';
import { IHui } from 'app/modules/hui/hui.model';
import { HuiService } from 'app/modules/hui/service/hui.service';
import { IHuiVien } from 'app/modules/hui-vien/hui-vien.model';
import { HuiVienService } from 'app/modules/hui-vien/service/hui-vien.service';

@Component({
  selector: 'jhi-chi-tiet-hui-update',
  templateUrl: './chi-tiet-hui-update.component.html',
})
export class ChiTietHuiUpdateComponent implements OnInit {
  isSaving = false;
  chiTietHui: IChiTietHui | null = null;

  huisSharedCollection: IHui[] = [];
  huiViensSharedCollection: Pick<IHuiVien, 'id'>[] = [];

  editForm: ChiTietHuiFormGroup = this.chiTietHuiFormService.createChiTietHuiFormGroup();

  constructor(
    protected chiTietHuiService: ChiTietHuiService,
    protected chiTietHuiFormService: ChiTietHuiFormService,
    protected huiService: HuiService,
    protected huiVienService: HuiVienService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareHui = (o1: IHui | null, o2: IHui | null): boolean => this.huiService.compareHui(o1, o2);

  compareHuiVien = (o1: IHuiVien | null, o2: IHuiVien | null): boolean => this.huiVienService.compareHuiVien(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chiTietHui }) => {
      this.chiTietHui = chiTietHui;
      if (chiTietHui) {
        this.updateForm(chiTietHui);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chiTietHui = this.chiTietHuiFormService.getChiTietHui(this.editForm);
    if (chiTietHui.id !== null) {
      this.subscribeToSaveResponse(this.chiTietHuiService.update(chiTietHui));
    } else {
      this.subscribeToSaveResponse(this.chiTietHuiService.create(chiTietHui));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChiTietHui>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(chiTietHui: IChiTietHui): void {
    this.chiTietHui = chiTietHui;
    this.chiTietHuiFormService.resetForm(this.editForm, chiTietHui);

    this.huisSharedCollection = this.huiService.addHuiToCollectionIfMissing<IHui>(this.huisSharedCollection, chiTietHui.hui);
    this.huiViensSharedCollection = this.huiVienService.addHuiVienToCollectionIfMissing<Pick<IHuiVien, 'id'>>(
      this.huiViensSharedCollection,
      chiTietHui.huiVien
    );
  }

  protected loadRelationshipsOptions(): void {
    this.huiService
      .query()
      .pipe(map((res: HttpResponse<IHui[]>) => res.body ?? []))
      .pipe(map((huis: IHui[]) => this.huiService.addHuiToCollectionIfMissing<IHui>(huis, this.chiTietHui?.hui)))
      .subscribe((huis: IHui[]) => (this.huisSharedCollection = huis));

    this.huiVienService
      .query()
      .pipe(map((res: HttpResponse<IHuiVien[]>) => res.body ?? []))
      .pipe(
        map((huiViens: IHuiVien[]) => this.huiVienService.addHuiVienToCollectionIfMissing<Pick<IHuiVien, 'id'>>(huiViens as Pick<IHuiVien, 'id'>[], this.chiTietHui?.huiVien))
      )
      .subscribe((huiViens: Pick<IHuiVien, 'id'>[]) => (this.huiViensSharedCollection = huiViens));
  }
}
