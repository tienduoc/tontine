import { IHui } from 'app/modules/hui/hui.model';
import { IHuiVien } from 'app/modules/hui-vien/hui-vien.model';
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
