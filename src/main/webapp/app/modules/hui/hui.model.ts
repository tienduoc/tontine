import dayjs from 'dayjs/esm';
import { LoaiHui } from 'app/entities/enumerations/loai-hui.model';
import { IHuiVien } from '../hui-vien/hui-vien.model';

export interface IHui {
  id: number;
  tenHui?: string | null;
  ngayTao?: dayjs.Dayjs | null;
  loaiHui?: LoaiHui | null;
  dayHui?: number | null;
  thamKeu?: number | null;
  soPhan?: number | null;
  chiTietHuis?:
    | [
        {
          id: number;
          hui: IHui;
          huiVien: IHuiVien;
          ngayKhui: string;
          thamKeu: number;
        }
      ]
    | null;
  huiViens?: any | null;
}

export type NewHui = Omit<IHui, 'id'> & { id: null };
