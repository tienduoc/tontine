import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { HuiFormService, HuiFormGroup } from './hui-form.service';
import { IHui } from '../hui.model';
import { HuiService } from '../service/hui.service';
import { LoaiHui } from 'app/entities/enumerations/loai-hui.model';

@Component({
  selector: 'jhi-hui-update',
  templateUrl: './hui-update.component.html',
})
export class HuiUpdateComponent implements OnInit {
  isSaving = false;
  hui: IHui | null = null;
  loaiHuiValues = Object.keys(LoaiHui);

  editForm: HuiFormGroup = this.huiFormService.createHuiFormGroup();

  constructor(protected huiService: HuiService, protected huiFormService: HuiFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hui }) => {
      this.hui = hui;
      if (hui) {
        this.updateForm(hui);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hui = this.huiFormService.getHui(this.editForm);
    if (hui.id !== null) {
      this.subscribeToSaveResponse(this.huiService.update(hui));
    } else {
      this.subscribeToSaveResponse(this.huiService.create(hui));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHui>>): void {
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

  protected updateForm(hui: IHui): void {
    this.hui = hui;
    this.huiFormService.resetForm(this.editForm, hui);
  }
}
