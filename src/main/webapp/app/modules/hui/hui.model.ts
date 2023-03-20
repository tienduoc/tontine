import dayjs from 'dayjs/esm';
import { LoaiHui } from 'app/entities/enumerations/loai-hui.model';

export interface IHui {
  id: number;
  tenHui?: string | null;
  ngayTao?: dayjs.Dayjs | null;
  loaiHui?: LoaiHui | null;
  dayHui?: number | null;
  thamKeu?: number | null;
  soPhan?: number | null;
}

export type NewHui = Omit<IHui, 'id'> & { id: null };
