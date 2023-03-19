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
import { MatDialog } from '@angular/material/dialog';

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
    protected modalService: NgbModal,
    public dialog: MatDialog
  ) {}

  openDialog() {
    console.log('121212');

    const dialogRef = this.dialog.open(DialogContentExampleDialog, {
      maxWidth: '100vw',
      maxHeight: '100vh',
      height: '100%',
      width: '100%',
      panelClass: 'full-screen-modal',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

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

@Component({
  selector: 'dialog-content-example-dialog',
  template: `<h2 mat-dialog-title color="primary">Install Angular</h2>
    <mat-dialog-content class="mat-typography">
      <h3>Develop across all platforms</h3>
      <p>
        Learn one way to build applications with Angular and reuse your code and abilities to build apps for any deployment target. For web,
        mobile web, native mobile and native desktop.
      </p>

      <h3>Speed &amp; Performance</h3>
      <p>
        Achieve the maximum speed possible on the Web Platform today, and take it further, via Web Workers and server-side rendering.
        Angular puts you in control over scalability. Meet huge data requirements by building data models on RxJS, Immutable.js or another
        push-model.
      </p>

      <h3>Incredible tooling</h3>
      <p>
        Build features quickly with simple, declarative templates. Extend the template language with your own components and use a wide
        array of existing components. Get immediate Angular-specific help and feedback with nearly every IDE and editor. All this comes
        together so you can focus on building amazing apps rather than trying to make the code work.
      </p>

      <h3>Loved by millions</h3>
      <p>
        From prototype through global deployment, Angular delivers the productivity and scalable infrastructure that supports Google's
        largest applications.
      </p>

      <h3>What is Angular?</h3>

      <p>
        Angular is a platform that makes it easy to build applications with the web. Angular combines declarative templates, dependency
        injection, end to end tooling, and integrated best practices to solve development challenges. Angular empowers developers to build
        applications that live on the web, mobile, or the desktop
      </p>

      <h3>Architecture overview</h3>

      <p>
        Angular is a platform and framework for building client applications in HTML and TypeScript. Angular is itself written in
        TypeScript. It implements core and optional functionality as a set of TypeScript libraries that you import into your apps.
      </p>

      <p>
        The basic building blocks of an Angular application are NgModules, which provide a compilation context for components. NgModules
        collect related code into functional sets; an Angular app is defined by a set of NgModules. An app always has at least a root module
        that enables bootstrapping, and typically has many more feature modules.
      </p>

      <p>
        Components define views, which are sets of screen elements that Angular can choose among and modify according to your program logic
        and data. Every app has at least a root component.
      </p>

      <p>
        Components use services, which provide specific functionality not directly related to views. Service providers can be injected into
        components as dependencies, making your code modular, reusable, and efficient.
      </p>

      <p>
        Both components and services are simply classes, with decorators that mark their type and provide metadata that tells Angular how to
        use them.
      </p>

      <p>
        The metadata for a component class associates it with a template that defines a view. A template combines ordinary HTML with Angular
        directives and binding markup that allow Angular to modify the HTML before rendering it for display.
      </p>

      <p>
        The metadata for a service class provides the information Angular needs to make it available to components through Dependency
        Injection (DI).
      </p>

      <p>
        An app's components typically define many views, arranged hierarchically. Angular provides the Router service to help you define
        navigation paths among views. The router provides sophisticated in-browser navigational capabilities.
      </p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Cancel</button>
      <button mat-button [mat-dialog-close]="true" cdkFocusInitial>Install</button>
    </mat-dialog-actions>`,
})
export class DialogContentExampleDialog {}
