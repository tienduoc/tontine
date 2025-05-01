import { Component, OnInit, ViewChild } from '@angular/core';
import { DsHuiKhuiService } from '../service/ds-hui-khui.service';
import { FormControl } from '@angular/forms';
import { startWith } from 'rxjs/operators';
import { Observable, switchMap } from 'rxjs';

@Component({
  selector: 'jhi-ds-hui-khui',
  templateUrl: './ds-hui-khui.component.html',
  styleUrls: ['./ds-hui-khui.component.css'],
})
export class DsHuiKhuiComponent implements OnInit {
  dsHuiKhui: any;
  dateControl: FormControl<any>;
  selectedDate: string;

  @ViewChild('screen', { static: true }) screen: any;

  constructor(private dsHuiKhuiService: DsHuiKhuiService) {
    this.dateControl = new FormControl(new Date().toISOString());
    // Initialize the selectedDate from localStorage or with the current date
    const today = new Date();
    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = today.getFullYear();
    const defaultDate = `${year}${month}${day}`;
    this.selectedDate = localStorage.getItem('selectedDate') ?? defaultDate;
  }

  ngOnInit(): void {
    this.dateControl.valueChanges
      .pipe(
        startWith(new Date()),
        switchMap((dateSelected: Date) => {
          const day = String(dateSelected.getDate()).padStart(2, '0');
          const month = String(dateSelected.getMonth() + 1).padStart(2, '0');
          const year = dateSelected.getFullYear();

          const stringDate = `${year}${month}${day}`;
          localStorage.setItem('selectedDate', stringDate);
          this.selectedDate = stringDate;
          return this.getDsHuiKhui(Number(stringDate));
        })
      )
      .subscribe(data => {
        this.dsHuiKhui = data.body ?? [];
      });
  }

  getDsHuiKhui(selectedDate: number): Observable<any> {
    return this.dsHuiKhuiService.getDsHuiKhui(selectedDate);
  }
}
