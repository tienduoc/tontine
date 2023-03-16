import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { BaseComponent } from 'app/components/base-component/base.component';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
}) export class HomeComponent extends BaseComponent implements OnInit, OnDestroy {
  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService, 
    private router: Router
  ) {
    super();
  }

  ngOnInit(): void {
    this.getAccount();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  private getAccount(): void {
    this.accountService
        .getAuthenticationState()
        .pipe(takeUntil(this.destroy$))
        .subscribe(account => (this.account = account));
  }
}
