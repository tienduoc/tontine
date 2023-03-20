import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IChiTietHui, NewChiTietHui } from '../chi-tiet-hui.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChiTietHui for edit and NewChiTietHuiFormGroupInput for create.
 */
type ChiTietHuiFormGroupInput = IChiTietHui | PartialWithRequiredKeyOf<NewChiTietHui>;

type ChiTietHuiFormDefaults = Pick<NewChiTietHui, 'id'>;

type ChiTietHuiFormGroupContent = {
  id: FormControl<IChiTietHui['id'] | NewChiTietHui['id']>;
  hui: FormControl<IChiTietHui['hui']>;
  huiVien: FormControl<IChiTietHui['huiVien']>;
};

export type ChiTietHuiFormGroup = FormGroup<ChiTietHuiFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChiTietHuiFormService {
  createChiTietHuiFormGroup(chiTietHui: ChiTietHuiFormGroupInput = { id: null }): ChiTietHuiFormGroup {
    const chiTietHuiRawValue = {
      ...this.getFormDefaults(),
      ...chiTietHui,
    };
    return new FormGroup<ChiTietHuiFormGroupContent>({
      id: new FormControl(
        { value: chiTietHuiRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      hui: new FormControl(chiTietHuiRawValue.hui),
      huiVien: new FormControl(chiTietHuiRawValue.huiVien),
    });
  }

  getChiTietHui(form: ChiTietHuiFormGroup): IChiTietHui | NewChiTietHui {
    return form.getRawValue() as IChiTietHui | NewChiTietHui;
  }

  resetForm(form: ChiTietHuiFormGroup, chiTietHui: ChiTietHuiFormGroupInput): void {
    const chiTietHuiRawValue = { ...this.getFormDefaults(), ...chiTietHui };
    form.reset(
      {
        ...chiTietHuiRawValue,
        id: { value: chiTietHuiRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ChiTietHuiFormDefaults {
    return {
      id: null,
    };
  }
}
