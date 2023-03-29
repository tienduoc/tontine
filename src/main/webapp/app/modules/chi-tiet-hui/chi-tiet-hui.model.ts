import { IHui } from 'app/entities/hui/hui.model';
import { IHuiVien } from 'app/entities/hui-vien/hui-vien.model';
import dayjs from 'dayjs/esm';

export interface IChiTietHui {
  id: number;
  hui?: Pick<IHui, 'id'> | null;
  huiVien?: Pick<IHuiVien, 'id'> | null;
  thamKeu?: number | null;
  ngayKhui?: dayjs.Dayjs | null;
  ky?: number | null;
}

export type NewChiTietHui = Omit<IChiTietHui, 'id'> & { id: null };
