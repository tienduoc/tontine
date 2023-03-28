export interface IHuiVien {
  id: number;
  hoTen?: string | null;
  sdt?: string | null;
  number?: number | null;
  thamkeu?: number | null;
  isDisableInput?: boolean;
  chiTietHuis: any;
}

export type NewHuiVien = Omit<IHuiVien, 'id'> & { id: null };
