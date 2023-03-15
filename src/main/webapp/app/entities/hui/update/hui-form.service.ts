import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHui, NewHui } from '../hui.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHui for edit and NewHuiFormGroupInput for create.
 */
type HuiFormGroupInput = IHui | PartialWithRequiredKeyOf<NewHui>;

type HuiFormDefaults = Pick<NewHui, 'id'>;

type HuiFormGroupContent = {
  id: FormControl<IHui['id'] | NewHui['id']>;
  tenHui: FormControl<IHui['tenHui']>;
  loaiHui: FormControl<IHui['loaiHui']>;
  dayHui: FormControl<IHui['dayHui']>;
  kiHienTai: FormControl<IHui['kiHienTai']>;
  phanChoi: FormControl<IHui['phanChoi']>;
};

export type HuiFormGroup = FormGroup<HuiFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HuiFormService {
  createHuiFormGroup(hui: HuiFormGroupInput = { id: null }): HuiFormGroup {
    const huiRawValue = {
      ...this.getFormDefaults(),
      ...hui,
    };
    return new FormGroup<HuiFormGroupContent>({
      id: new FormControl(
        { value: huiRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tenHui: new FormControl(huiRawValue.tenHui),
      loaiHui: new FormControl(huiRawValue.loaiHui),
      dayHui: new FormControl(huiRawValue.dayHui),
      kiHienTai: new FormControl(huiRawValue.kiHienTai),
      phanChoi: new FormControl(huiRawValue.phanChoi),
    });
  }

  getHui(form: HuiFormGroup): IHui | NewHui {
    return form.getRawValue() as IHui | NewHui;
  }

  resetForm(form: HuiFormGroup, hui: HuiFormGroupInput): void {
    const huiRawValue = { ...this.getFormDefaults(), ...hui };
    form.reset(
      {
        ...huiRawValue,
        id: { value: huiRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HuiFormDefaults {
    return {
      id: null,
    };
  }
}
