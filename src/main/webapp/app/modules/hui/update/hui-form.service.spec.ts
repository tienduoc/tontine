import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hui.test-samples';

import { HuiFormService } from './hui-form.service';

describe('Hui Form Service', () => {
  let service: HuiFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HuiFormService);
  });

  describe('Service methods', () => {
    describe('createHuiFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHuiFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tenHui: expect.any(Object),
            loaiHui: expect.any(Object),
            dayHui: expect.any(Object),
            kiHienTai: expect.any(Object),
            phanChoi: expect.any(Object),
          })
        );
      });

      it('passing IHui should create a new form with FormGroup', () => {
        const formGroup = service.createHuiFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tenHui: expect.any(Object),
            loaiHui: expect.any(Object),
            dayHui: expect.any(Object),
            kiHienTai: expect.any(Object),
            phanChoi: expect.any(Object),
          })
        );
      });
    });

    describe('getHui', () => {
      it('should return NewHui for default Hui initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHuiFormGroup(sampleWithNewData);

        const hui = service.getHui(formGroup) as any;

        expect(hui).toMatchObject(sampleWithNewData);
      });

      it('should return NewHui for empty Hui initial value', () => {
        const formGroup = service.createHuiFormGroup();

        const hui = service.getHui(formGroup) as any;

        expect(hui).toMatchObject({});
      });

      it('should return IHui', () => {
        const formGroup = service.createHuiFormGroup(sampleWithRequiredData);

        const hui = service.getHui(formGroup) as any;

        expect(hui).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHui should not enable id FormControl', () => {
        const formGroup = service.createHuiFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHui should disable id FormControl', () => {
        const formGroup = service.createHuiFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
