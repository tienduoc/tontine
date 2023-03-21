import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IChiTietHui, NewChiTietHui } from '../chi-tiet-hui.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm';
import { map } from 'rxjs/operators';
import { RestHui } from '../../hui/service/hui.service';

export type PartialUpdateChiTietHui = Partial<IChiTietHui> & Pick<IChiTietHui, 'id'>;

type RestOf<T extends IChiTietHui | NewChiTietHui> = Omit<T, 'ngayKhui'> & {
  ngayKhui?: string | null;
};

export type RestChiTietHui = RestOf<IChiTietHui>;
export type EntityResponseType = HttpResponse<IChiTietHui>;
export type EntityArrayResponseType = HttpResponse<IChiTietHui[]>;

@Injectable({ providedIn: 'root' })
export class ChiTietHuiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chi-tiet-huis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chiTietHui: NewChiTietHui): Observable<EntityResponseType> {
    return this.http
      .post<RestChiTietHui>(this.resourceUrl, chiTietHui, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(chiTietHui: IChiTietHui): Observable<EntityResponseType> {
    return this.http.put<IChiTietHui>(`${this.resourceUrl}/${this.getChiTietHuiIdentifier(chiTietHui)}`, chiTietHui, {
      observe: 'response',
    });
  }

  partialUpdate(chiTietHui: PartialUpdateChiTietHui): Observable<EntityResponseType> {
    return this.http.patch<IChiTietHui>(`${this.resourceUrl}/${this.getChiTietHuiIdentifier(chiTietHui)}`, chiTietHui, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChiTietHui>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestChiTietHui[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChiTietHuiIdentifier(chiTietHui: Pick<IChiTietHui, 'id'>): number {
    return chiTietHui.id;
  }

  compareChiTietHui(o1: Pick<IChiTietHui, 'id'> | null, o2: Pick<IChiTietHui, 'id'> | null): boolean {
    return o1 && o2 ? this.getChiTietHuiIdentifier(o1) === this.getChiTietHuiIdentifier(o2) : o1 === o2;
  }

  addChiTietHuiToCollectionIfMissing<Type extends Pick<IChiTietHui, 'id'>>(
    chiTietHuiCollection: Type[],
    ...chiTietHuisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const chiTietHuis: Type[] = chiTietHuisToCheck.filter(isPresent);
    if (chiTietHuis.length > 0) {
      const chiTietHuiCollectionIdentifiers = chiTietHuiCollection.map(chiTietHuiItem => this.getChiTietHuiIdentifier(chiTietHuiItem)!);
      const chiTietHuisToAdd = chiTietHuis.filter(chiTietHuiItem => {
        const chiTietHuiIdentifier = this.getChiTietHuiIdentifier(chiTietHuiItem);
        if (chiTietHuiCollectionIdentifiers.includes(chiTietHuiIdentifier)) {
          return false;
        }
        chiTietHuiCollectionIdentifiers.push(chiTietHuiIdentifier);
        return true;
      });
      return [...chiTietHuisToAdd, ...chiTietHuiCollection];
    }
    return chiTietHuiCollection;
  }

  protected convertDateFromClient<T extends IChiTietHui | NewChiTietHui | PartialUpdateChiTietHui>(chiTietHui: T): RestOf<T> {
    return {
      ...chiTietHui,
      ngayKhui: chiTietHui.ngayKhui?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restChiTietHui: RestChiTietHui): IChiTietHui {
    return {
      ...restChiTietHui,
      ngayKhui: restChiTietHui.ngayKhui ? dayjs(restChiTietHui.ngayKhui) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestChiTietHui>): HttpResponse<IChiTietHui> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestChiTietHui[]>): HttpResponse<IChiTietHui[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
