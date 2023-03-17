import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHuiVien, NewHuiVien } from '../hui-vien.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHuiVien for edit and NewHuiVienFormGroupInput for create.
 */
type HuiVienFormGroupInput = IHuiVien | PartialWithRequiredKeyOf<NewHuiVien>;

type HuiVienFormDefaults = Pick<NewHuiVien, 'id'>;

type HuiVienFormGroupContent = {
  id: FormControl<IHuiVien['id'] | NewHuiVien['id']>;
  hoTen: FormControl<IHuiVien['hoTen']>;
  sdt: FormControl<IHuiVien['sdt']>;
};

export type HuiVienFormGroup = FormGroup<HuiVienFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HuiVienFormService {
  createHuiVienFormGroup(huiVien: HuiVienFormGroupInput = { id: null }): HuiVienFormGroup {
    const huiVienRawValue = {
      ...this.getFormDefaults(),
      ...huiVien,
    };
    return new FormGroup<HuiVienFormGroupContent>({
      id: new FormControl(
        { value: huiVienRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      hoTen: new FormControl(huiVienRawValue.hoTen),
      sdt: new FormControl(huiVienRawValue.sdt),
    });
  }

  getHuiVien(form: HuiVienFormGroup): IHuiVien | NewHuiVien {
    return form.getRawValue() as IHuiVien | NewHuiVien;
  }

  resetForm(form: HuiVienFormGroup, huiVien: HuiVienFormGroupInput): void {
    const huiVienRawValue = { ...this.getFormDefaults(), ...huiVien };
    form.reset(
      {
        ...huiVienRawValue,
        id: { value: huiVienRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HuiVienFormDefaults {
    return {
      id: null,
    };
  }
}
