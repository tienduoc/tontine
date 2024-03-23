import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHui, NewHui } from '../hui.model';

export type PartialUpdateHui = Partial<IHui> & Pick<IHui, 'id'>;

type RestOf<T extends IHui | NewHui> = Omit<T, 'ngayTao'> & {
  ngayTao?: string | null;
};

export type RestHui = RestOf<IHui>;

export type NewRestHui = RestOf<NewHui>;

export type PartialUpdateRestHui = RestOf<PartialUpdateHui>;

export type EntityResponseType = HttpResponse<IHui>;
export type EntityArrayResponseType = HttpResponse<IHui[]>;

export type ResThongKe = {
  soHuiSong: number;
  soHuiChet: number;
};

@Injectable({ providedIn: 'root' })
export class HuiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/huis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getThongKe(page = 0, size = 20): Observable<any> {
    return this.http.get(`${this.resourceUrl}/thongke?page=${page}&size=${size}`, { observe: 'response' });
  }

  create(hui: NewHui): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hui);
    return this.http.post<RestHui>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(hui: IHui): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hui);
    return this.http
      .put<RestHui>(`${this.resourceUrl}/${this.getHuiIdentifier(hui)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(hui: PartialUpdateHui): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hui);
    return this.http
      .patch<RestHui>(`${this.resourceUrl}/${this.getHuiIdentifier(hui)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHui>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHui[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHuiIdentifier(hui: Pick<IHui, 'id'>): number {
    return hui.id;
  }

  compareHui(o1: Pick<IHui, 'id'> | null, o2: Pick<IHui, 'id'> | null): boolean {
    return o1 && o2 ? this.getHuiIdentifier(o1) === this.getHuiIdentifier(o2) : o1 === o2;
  }

  addHuiToCollectionIfMissing<Type extends Pick<IHui, 'id'>>(huiCollection: Type[], ...huisToCheck: (Type | null | undefined)[]): Type[] {
    const huis: Type[] = huisToCheck.filter(isPresent);
    if (huis.length > 0) {
      const huiCollectionIdentifiers = huiCollection.map(huiItem => this.getHuiIdentifier(huiItem)!);
      const huisToAdd = huis.filter(huiItem => {
        const huiIdentifier = this.getHuiIdentifier(huiItem);
        if (huiCollectionIdentifiers.includes(huiIdentifier)) {
          return false;
        }
        huiCollectionIdentifiers.push(huiIdentifier);
        return true;
      });
      return [...huisToAdd, ...huiCollection];
    }
    return huiCollection;
  }

  protected convertDateFromClient<T extends IHui | NewHui | PartialUpdateHui>(hui: T): RestOf<T> {
    return {
      ...hui,
      ngayTao: hui.ngayTao?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restHui: RestHui): IHui {
    return {
      ...restHui,
      ngayTao: restHui.ngayTao ? dayjs(restHui.ngayTao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHui>): HttpResponse<IHui> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHui[]>): HttpResponse<IHui[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
