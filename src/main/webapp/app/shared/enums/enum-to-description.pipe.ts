import { Pipe, PipeTransform } from '@angular/core';

enum EnumLoaiHuiString {
  NGAY = 'ngày',

  TUAN = 'tuần',

  MUOI_NGAY = '10 ngày',

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

    if (value === 'MUOI_NGAY') {
      return '10 ngày';
    }

    if (value === 'NUA_THANG') {
      return 'Nửa tháng';
    }

    if (value === 'THANG') {
      return 'Tháng';
    }

    return '';
  }
}
