export interface IHuiVien {
  id: number;
  hoTen?: string | null;
  sdt?: string | null;
}

export type NewHuiVien = Omit<IHuiVien, 'id'> & { id: null };
