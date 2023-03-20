import { IHui } from 'app/entities/hui/hui.model';
import { IHuiVien } from 'app/entities/hui-vien/hui-vien.model';

export interface IChiTietHui {
  id: number;
  hui?: Pick<IHui, 'id'> | null;
  huiVien?: Pick<IHuiVien, 'id'> | null;
}

export type NewChiTietHui = Omit<IChiTietHui, 'id'> & { id: null };
