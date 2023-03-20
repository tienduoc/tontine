import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChiTietHui } from '../chi-tiet-hui.model';
import { ChiTietHuiService } from '../service/chi-tiet-hui.service';

@Injectable({ providedIn: 'root' })
export class ChiTietHuiRoutingResolveService implements Resolve<IChiTietHui | null> {
  constructor(protected service: ChiTietHuiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChiTietHui | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chiTietHui: HttpResponse<IChiTietHui>) => {
          if (chiTietHui.body) {
            return of(chiTietHui.body);
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
