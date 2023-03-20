import { IChiTietHui, NewChiTietHui } from './chi-tiet-hui.model';

export const sampleWithRequiredData: IChiTietHui = {
  id: 39704,
};

export const sampleWithPartialData: IChiTietHui = {
  id: 48706,
};

export const sampleWithFullData: IChiTietHui = {
  id: 57732,
};

export const sampleWithNewData: NewChiTietHui = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
