import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../hui-vien.test-samples';

import { HuiVienFormService } from './hui-vien-form.service';

describe('HuiVien Form Service', () => {
  let service: HuiVienFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HuiVienFormService);
  });

  describe('Service methods', () => {
    describe('createHuiVienFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHuiVienFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hoTen: expect.any(Object),
            sdt: expect.any(Object),
          })
        );
      });

      it('passing IHuiVien should create a new form with FormGroup', () => {
        const formGroup = service.createHuiVienFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            hoTen: expect.any(Object),
            sdt: expect.any(Object),
          })
        );
      });
    });

    describe('getHuiVien', () => {
      it('should return NewHuiVien for default HuiVien initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHuiVienFormGroup(sampleWithNewData);

        const huiVien = service.getHuiVien(formGroup) as any;

        expect(huiVien).toMatchObject(sampleWithNewData);
      });

      it('should return NewHuiVien for empty HuiVien initial value', () => {
        const formGroup = service.createHuiVienFormGroup();

        const huiVien = service.getHuiVien(formGroup) as any;

        expect(huiVien).toMatchObject({});
      });

      it('should return IHuiVien', () => {
        const formGroup = service.createHuiVienFormGroup(sampleWithRequiredData);

        const huiVien = service.getHuiVien(formGroup) as any;

        expect(huiVien).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHuiVien should not enable id FormControl', () => {
        const formGroup = service.createHuiVienFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHuiVien should disable id FormControl', () => {
        const formGroup = service.createHuiVienFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
