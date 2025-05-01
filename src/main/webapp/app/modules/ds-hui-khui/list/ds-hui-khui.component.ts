import { Component, OnInit, ViewChild } from '@angular/core';
import { DsHuiKhuiService } from '../service/ds-hui-khui.service';
import { FormControl } from '@angular/forms';
import { startWith } from 'rxjs/operators';
import { Observable, switchMap, tap } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';

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

  constructor(
    private dsHuiKhuiService: DsHuiKhuiService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.dateControl = new FormControl(new Date().toISOString());
    // Default to current date
    const today = new Date();
    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const year = today.getFullYear();
    this.selectedDate = `${year}-${month}-${day}`;
  }

  ngOnInit(): void {
    // Read date from query params or use service's stored date
    this.route.queryParams.subscribe(params => {
      if (params['date']) {
        // If date is in query params, use it
        this.selectedDate = params['date'];
        this.dsHuiKhuiService.setSelectedDate(this.selectedDate);

        // Update date control to match the query param date
        const [year, month, day] = this.selectedDate.split('-');
        const date = new Date(Number(year), Number(month) - 1, Number(day));
        this.dateControl.setValue(date.toISOString(), { emitEvent: false });

        // Fetch data with the date from query params
        const apiDate = Number(year + month + day);
        this.getDsHuiKhui(apiDate).subscribe(data => {
          this.dsHuiKhui = data.body ?? [];
        });
      } else {
        // If no date in query params, use the service's date or default
        this.selectedDate = this.dsHuiKhuiService.getSelectedDate();

        // Update URL with the selected date
        this.updateUrlWithDate(this.selectedDate);

        // Update date control to match the selected date
        const [year, month, day] = this.selectedDate.split('-');
        const date = new Date(Number(year), Number(month) - 1, Number(day));
        this.dateControl.setValue(date.toISOString(), { emitEvent: false });

        // Fetch data with the selected date
        const apiDate = Number(year + month + day);
        this.getDsHuiKhui(apiDate).subscribe(data => {
          this.dsHuiKhui = data.body ?? [];
        });
      }
    });

    // Handle date changes from the date picker
    this.dateControl.valueChanges
      .pipe(
        switchMap((dateSelected: Date) => {
          const day = String(dateSelected.getDate()).padStart(2, '0');
          const month = String(dateSelected.getMonth() + 1).padStart(2, '0');
          const year = dateSelected.getFullYear();

          const stringDate = `${year}${month}${day}`;
          const formattedDate = `${year}-${month}-${day}`;

          // Update the selected date in the service
          this.dsHuiKhuiService.setSelectedDate(formattedDate);
          this.selectedDate = formattedDate;

          // Update URL with the new date
          this.updateUrlWithDate(formattedDate);

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

  // Update URL with the selected date without navigating
  private updateUrlWithDate(date: string): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { date },
      queryParamsHandling: 'merge',
      replaceUrl: true // Don't create a new history entry
    });
  }
}
