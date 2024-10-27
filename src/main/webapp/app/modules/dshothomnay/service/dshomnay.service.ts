import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class DsHomnayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/huis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getThongKe(date: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}?date=${date}`, { observe: 'response' });
  }
}
