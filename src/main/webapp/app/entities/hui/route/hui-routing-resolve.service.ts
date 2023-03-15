import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHui } from '../hui.model';
import { HuiService } from '../service/hui.service';

@Injectable({ providedIn: 'root' })
export class HuiRoutingResolveService implements Resolve<IHui | null> {
  constructor(protected service: HuiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHui | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
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
