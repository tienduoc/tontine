import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { HuiVienFormService, HuiVienFormGroup } from './hui-vien-form.service';
import { IHuiVien } from '../hui-vien.model';
import { HuiVienService } from '../service/hui-vien.service';

@Component({
  selector: 'jhi-hui-vien-update',
  templateUrl: './hui-vien-update.component.html',
})
export class HuiVienUpdateComponent implements OnInit {
  isSaving = false;
  huiVien: IHuiVien | null = null;

  editForm: HuiVienFormGroup = this.huiVienFormService.createHuiVienFormGroup();

  constructor(
    protected huiVienService: HuiVienService,
    protected huiVienFormService: HuiVienFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ huiVien }) => {
      this.huiVien = huiVien;
      if (huiVien) {
        this.updateForm(huiVien);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const huiVien = this.huiVienFormService.getHuiVien(this.editForm);
    if (huiVien.id !== null) {
      this.subscribeToSaveResponse(this.huiVienService.update(huiVien));
    } else {
      this.subscribeToSaveResponse(this.huiVienService.create(huiVien));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHuiVien>>): void {
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

  protected updateForm(huiVien: IHuiVien): void {
    this.huiVien = huiVien;
    this.huiVienFormService.resetForm(this.editForm, huiVien);
  }
}
