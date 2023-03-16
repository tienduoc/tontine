import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHui, NewHui } from '../hui.model';

export type PartialUpdateHui = Partial<IHui> & Pick<IHui, 'id'>;

export type EntityResponseType = HttpResponse<IHui>;
export type EntityArrayResponseType = HttpResponse<IHui[]>;

@Injectable({ providedIn: 'root' })
export class HuiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/huis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hui: NewHui): Observable<EntityResponseType> {
    return this.http.post<IHui>(this.resourceUrl, hui, { observe: 'response' });
  }

  update(hui: IHui): Observable<EntityResponseType> {
    return this.http.put<IHui>(`${this.resourceUrl}/${this.getHuiIdentifier(hui)}`, hui, { observe: 'response' });
  }

  partialUpdate(hui: PartialUpdateHui): Observable<EntityResponseType> {
    return this.http.patch<IHui>(`${this.resourceUrl}/${this.getHuiIdentifier(hui)}`, hui, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHui>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHui[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
