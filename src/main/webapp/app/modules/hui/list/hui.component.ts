import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, finalize, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHui } from '../hui.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, HuiService } from '../service/hui.service';
import { HuiDeleteDialogComponent } from '../delete/hui-delete-dialog.component';
import { ColumnMode } from '@swimlane/ngx-datatable';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
  symbol1: string;
  symbol2: string;
  symbol3: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H', symbol1: 'H12313', symbol2: 'H12313', symbol3: 'H123113123112' },
  { position: 2, name: 'Helium', weight: 4.0026, symbol: 'He', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 5, name: 'Boron', weight: 10.811, symbol: 'B', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
  { position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne', symbol1: 'H', symbol2: 'H', symbol3: 'H' },
];

@Component({
  selector: 'jhi-hui',
  templateUrl: './hui.component.html',
})
export class HuiComponent implements OnInit {
  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol', 'symbol1', 'symbol2', 'symbol3'];
  dataSource = ELEMENT_DATA;
  huis?: IHui[];
  isLoading = false;

  ColumnMode = ColumnMode;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  rows = [
    { name: 'John', age: 25 },
    { name: 'Jane', age: 30 },
    { name: 'Bob', age: 40 },
  ];
  columns = [{ prop: 'name' }, { prop: 'age' }];

  loadingIndicator = true;
  reorderable = true;

  constructor(
    protected huiService: HuiService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IHui): number => this.huiService.getHuiIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(hui: IHui): void {
    const modalRef = this.modalService.open(HuiDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hui = hui;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.huis = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IHui[] | null): IHui[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.huiService.query(queryObject).pipe(finalize(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
