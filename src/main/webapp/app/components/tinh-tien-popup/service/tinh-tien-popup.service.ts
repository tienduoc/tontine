import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { map, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TinhTienService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hot-hui');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
