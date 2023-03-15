export interface IHui {
  id: number;
  tenHui?: string | null;
  loaiHui?: string | null;
  dayHui?: number | null;
  kiHienTai?: number | null;
  phanChoi?: number | null;
}

export type NewHui = Omit<IHui, 'id'> & { id: null };
