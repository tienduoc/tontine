import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../chi-tiet-hui.test-samples';

import { ChiTietHuiFormService } from './chi-tiet-hui-form.service';

describe('ChiTietHui Form Service', () => {
  let service: ChiTietHuiFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChiTietHuiFormService);
  });

  describe('Service methods', () => {
    describe('createChiTietHuiFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createChiTietHuiFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hui: expect.any(Object),
            huiVien: expect.any(Object),
          })
        );
      });

      it('passing IChiTietHui should create a new form with FormGroup', () => {
        const formGroup = service.createChiTietHuiFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hui: expect.any(Object),
            huiVien: expect.any(Object),
          })
        );
      });
    });

    describe('getChiTietHui', () => {
      it('should return NewChiTietHui for default ChiTietHui initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createChiTietHuiFormGroup(sampleWithNewData);

        const chiTietHui = service.getChiTietHui(formGroup) as any;

        expect(chiTietHui).toMatchObject(sampleWithNewData);
      });

      it('should return NewChiTietHui for empty ChiTietHui initial value', () => {
        const formGroup = service.createChiTietHuiFormGroup();

        const chiTietHui = service.getChiTietHui(formGroup) as any;

        expect(chiTietHui).toMatchObject({});
      });

      it('should return IChiTietHui', () => {
        const formGroup = service.createChiTietHuiFormGroup(sampleWithRequiredData);

        const chiTietHui = service.getChiTietHui(formGroup) as any;

        expect(chiTietHui).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IChiTietHui should not enable id FormControl', () => {
        const formGroup = service.createChiTietHuiFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewChiTietHui should disable id FormControl', () => {
        const formGroup = service.createChiTietHuiFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
