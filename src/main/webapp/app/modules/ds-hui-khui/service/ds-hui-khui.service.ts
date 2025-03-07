import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class DsHuiKhuiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ds-hui-khui');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getDsHuiKhui(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}`, { observe: 'response' });
  }
}
