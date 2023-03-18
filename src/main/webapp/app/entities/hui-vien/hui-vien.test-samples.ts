import { IHuiVien, NewHuiVien } from './hui-vien.model';

export const sampleWithRequiredData: IHuiVien = {
  id: 90496,
};

export const sampleWithPartialData: IHuiVien = {
  id: 16897,
  hoTen: 'New Incredible Borders',
};

export const sampleWithFullData: IHuiVien = {
  id: 9955,
  hoTen: 'Future',
  sdt: 'vertical virtual',
};

export const sampleWithNewData: NewHuiVien = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
