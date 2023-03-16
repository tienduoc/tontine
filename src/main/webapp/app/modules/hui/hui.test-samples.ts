import { IHui, NewHui } from './hui.model';

export const sampleWithRequiredData: IHui = {
  id: 840,
};

export const sampleWithPartialData: IHui = {
  id: 39618,
  loaiHui: 'fresh-thinking connect Frozen',
  phanChoi: 26235,
};

export const sampleWithFullData: IHui = {
  id: 50658,
  tenHui: 'Avon deliver rich',
  loaiHui: 'schemas throughput',
  dayHui: 70011,
  kiHienTai: 2499,
  phanChoi: 31964,
};

export const sampleWithNewData: NewHui = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
