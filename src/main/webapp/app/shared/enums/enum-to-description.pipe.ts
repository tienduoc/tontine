import { Pipe, PipeTransform } from '@angular/core';
import { LoaiHui } from 'app/entities/enumerations/loai-hui.model';

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
      return 'ngày';
    }

    if (value === 'TUAN') {
      return 'tuần';
    }

    if (value === 'NUA_THANG') {
      return 'nửa tháng';
    }

    return 'tháng';
  }
}
