import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs';

@Pipe({
  name: 'formatMediumDate'
})
export class FormatMediumDatePipe implements PipeTransform {
  transform(value: any): string {
    if (!value || !dayjs(value).isValid()) {
      return '';
    }
    return dayjs(value).format('DD-MM-YYYY');
  }
}
