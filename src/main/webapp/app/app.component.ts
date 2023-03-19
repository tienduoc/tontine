import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from './modules/login/login.service';

@Component({
  selector: 'app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  currentUser$ = this.accountService.getAuthenticationState();

  constructor(private accountService: AccountService, private router: Router, private loginService: LoginService) {}

  ngOnInit(): void {}

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }
}
