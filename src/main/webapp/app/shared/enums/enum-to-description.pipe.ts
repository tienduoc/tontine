import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'enumToDescription'
})
export class EnumToDescriptionPipe implements PipeTransform {
  private static readonly DESCRIPTIONS: Record<string, string> = {
    NGAY: 'Ngày',
    TUAN: 'Tuần',
    MUOI_NGAY: '10 ngày',
    NUA_THANG: 'Nửa tháng',
    THANG: 'Tháng',
  };

  transform(value: string): string {
    return this.getDescription(value);
  }

  private getDescription(value: string): string {
    return EnumToDescriptionPipe.DESCRIPTIONS[value] || '';
  }
}
