import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHuiVien } from '../hui-vien.model';
import { HuiVienService } from '../service/hui-vien.service';

@Injectable({ providedIn: 'root' })
export class HuiVienRoutingResolveService implements Resolve<IHuiVien | null> {
  constructor(protected service: HuiVienService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHuiVien | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((huiVien: HttpResponse<IHuiVien>) => {
          if (huiVien.body) {
            return of(huiVien.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
