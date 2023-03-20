import dayjs from 'dayjs/esm';

import { LoaiHui } from 'app/entities/enumerations/loai-hui.model';

import { IHui, NewHui } from './hui.model';

export const sampleWithRequiredData: IHui = {
  id: 840,
};

export const sampleWithPartialData: IHui = {
  id: 89107,
  ngayTao: dayjs('2023-03-18'),
  thamKeu: 41124,
};

export const sampleWithFullData: IHui = {
  id: 11050,
  tenHui: 'Cambridgeshire Coordinator Buckinghamshire',
  ngayTao: dayjs('2023-03-18'),
  loaiHui: LoaiHui['TUAN'],
  dayHui: 38378,
  thamKeu: 53914,
  soPhan: 47808,
};

export const sampleWithNewData: NewHui = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
