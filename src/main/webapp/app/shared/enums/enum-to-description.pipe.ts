import { Pipe, PipeTransform } from '@angular/core';

enum EnumLoaiHuiString {
  NGAY = 'ngày',

  TUAN = 'tuần',

  NUA_THANG = 'nửa tháng',

  THANG = 'tháng',
}

@Pipe({
  name: 'enumToDescription',
})
export class EnumToDescriptionPipe implements PipeTransform {
  transform(value: string): string {
    if (value === 'NGAY') {
      return 'Ngày';
    }

    if (value === 'TUAN') {
      return 'Tuần';
    }

    if (value === 'NUA_THANG') {
      return 'Nửa tháng';
    }

    return 'Tháng';
  }
}
