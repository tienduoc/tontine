import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHuiVien, NewHuiVien } from '../hui-vien.model';

export type PartialUpdateHuiVien = Partial<IHuiVien> & Pick<IHuiVien, 'id'>;

export type EntityResponseType = HttpResponse<IHuiVien>;
export type EntityArrayResponseType = HttpResponse<IHuiVien[]>;

@Injectable({ providedIn: 'root' })
export class HuiVienService {
  isLoading$ = new BehaviorSubject(false);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hui-viens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(huiVien: NewHuiVien): Observable<EntityResponseType> {
    return this.http.post<IHuiVien>(this.resourceUrl, huiVien, { observe: 'response' });
  }

  update(huiVien: IHuiVien): Observable<EntityResponseType> {
    return this.http.put<IHuiVien>(`${this.resourceUrl}/${this.getHuiVienIdentifier(huiVien)}`, huiVien, { observe: 'response' });
  }

  partialUpdate(huiVien: PartialUpdateHuiVien): Observable<EntityResponseType> {
    return this.http.patch<IHuiVien>(`${this.resourceUrl}/${this.getHuiVienIdentifier(huiVien)}`, huiVien, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHuiVien>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHuiVien[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHuiVienIdentifier(huiVien: Pick<IHuiVien, 'id'>): number {
    return huiVien.id;
  }

  compareHuiVien(o1: Pick<IHuiVien, 'id'> | null, o2: Pick<IHuiVien, 'id'> | null): boolean {
    return o1 && o2 ? this.getHuiVienIdentifier(o1) === this.getHuiVienIdentifier(o2) : o1 === o2;
  }

  addHuiVienToCollectionIfMissing<Type extends Pick<IHuiVien, 'id'>>(
    huiVienCollection: Type[],
    ...huiViensToCheck: (Type | null | undefined)[]
  ): Type[] {
    const huiViens: Type[] = huiViensToCheck.filter(isPresent);
    if (huiViens.length > 0) {
      const huiVienCollectionIdentifiers = huiVienCollection.map(huiVienItem => this.getHuiVienIdentifier(huiVienItem)!);
      const huiViensToAdd = huiViens.filter(huiVienItem => {
        const huiVienIdentifier = this.getHuiVienIdentifier(huiVienItem);
        if (huiVienCollectionIdentifiers.includes(huiVienIdentifier)) {
          return false;
        }
        huiVienCollectionIdentifiers.push(huiVienIdentifier);
        return true;
      });
      return [...huiViensToAdd, ...huiVienCollection];
    }
    return huiVienCollection;
  }
}
