import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IHui } from '../hui.model';

export type EntityResponseType = HttpResponse<IHui>;

@Injectable({ providedIn: 'root' })
export class DsHuiKhuiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ds-hui-khui');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getDsHuiKhui(date: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}?date=${date}`, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHui>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
