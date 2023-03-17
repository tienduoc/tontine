import { Component, OnInit, OnDestroy } from '@angular/core';
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

  constructor(
    private accountService: AccountService, 
  ) {
    super();
  }

  ngOnInit(): void {
    this.getAccount();
  }

  private getAccount(): void {
    this.accountService
        .getAuthenticationState()
        .pipe(takeUntil(this.destroyed$))
        .subscribe(account => (this.account = account));
  }
}
