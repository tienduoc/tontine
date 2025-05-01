import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IHui } from '../hui.model';

export type EntityResponseType = HttpResponse<IHui>;

@Injectable({ providedIn: 'root' })
export class DsHuiKhuiService {
// Protected members
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ds-hui-khui');

  // Private members
  private selectedDateSubject = new BehaviorSubject<string>(this.getFormattedCurrentDate());

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  // Public methods
  // Set the selected date
  setSelectedDate(date: string): void {
    this.selectedDateSubject.next(date);
  }

  // Get the current value of the selected date
  getSelectedDate(): string {
    return this.selectedDateSubject.getValue();
  }

  getDsHuiKhui(date: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}?date=${date}`, { observe: 'response' });
  }

  find(id: number, date?: string): Observable<EntityResponseType> {
    const url = date ? `${this.resourceUrl}/${id}?date=${date}` : `${this.resourceUrl}/${id}`;
    return this.http.get<IHui>(url, { observe: 'response' });
  }

  // Private methods
  // Get current date formatted as YYYY-MM-DD
  private getFormattedCurrentDate(): string {
    const today = new Date();
    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = today.getFullYear();
    return `${year}-${month}-${day}`;
  }
}
