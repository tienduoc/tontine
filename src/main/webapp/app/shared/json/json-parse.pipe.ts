import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'prettyprint',
})
export class PrettyPrintPipe implements PipeTransform {
  transform(val: string) {
    return JSON.parse(val);
  }
}
