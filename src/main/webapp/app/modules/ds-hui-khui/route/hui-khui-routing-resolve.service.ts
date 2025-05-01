import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHui } from '../hui.model';
import { DsHuiKhuiService } from '../service/ds-hui-khui.service';

@Injectable({ providedIn: 'root' })
export class HuiKhuiRoutingResolveService implements Resolve<IHui | null> {
  constructor(protected service: DsHuiKhuiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHui | null | never> {
    const id = route.params['id'];
    const date = route.queryParams['date'];
    if (id) {
      return this.service.find(id, date).pipe(
        mergeMap((hui: HttpResponse<IHui>) => {
          if (hui.body) {
            return of(hui.body);
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
