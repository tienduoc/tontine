import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { IHuiVien } from 'app/modules/hui-vien/hui-vien.model';

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
  ngayTao: FormControl<IHui['ngayTao']>;
  loaiHui: FormControl<IHui['loaiHui']>;
  dayHui: FormControl<IHui['dayHui']>;
  thamKeu: FormControl<IHui['thamKeu']>;
  soPhan: FormControl<IHui['soPhan']>;
  huiViens: FormControl<IHui['huiViens']>;
};

export type HuiFormGroup = FormGroup<HuiFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HuiFormService {
  createHuiFormGroup(hui: HuiFormGroupInput = { id: null }): HuiFormGroup {
    const huiRawValue = {
      ...this.getFormDefaults(),
      ...hui,
      huiViens: this.generateHuiViensOptions(hui as IHui),
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
      ngayTao: new FormControl(huiRawValue.ngayTao),
      loaiHui: new FormControl(huiRawValue.loaiHui),
      dayHui: new FormControl(huiRawValue.dayHui),
      thamKeu: new FormControl(huiRawValue.thamKeu),
      soPhan: new FormControl(huiRawValue.soPhan),
      huiViens: new FormControl(huiRawValue.huiViens),
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
        huiViens: this.generateHuiViensOptions(hui as IHui),
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private generateHuiViensOptions(hui: IHui): IHuiVien[] | undefined {
    return hui.chiTietHuis?.map(chiTietHui => {
      return chiTietHui.huiVien;
    });
  }

  private getFormDefaults(): HuiFormDefaults {
    return {
      id: null,
    };
  }
}
