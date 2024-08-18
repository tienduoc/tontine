export interface IHuiVien {
  id: number;
  hoTen?: string | null;
  sdt?: string | null;
  number?: number | null;
  thamkeu?: number | null;
  isDisableInput?: boolean;
  chiTietHuis: any;
  tongHuiSong: number | null;
  tongHuiChet: number | null;
}

export type NewHuiVien = Omit<IHuiVien, 'id'> & { id: null };
