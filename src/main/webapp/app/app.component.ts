import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';

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
  @ViewChild('drawer', { static: false }) usuarioMenu!: MatSidenav;

  constructor(
    private matSidenav: MatSidenav,
    private accountService: AccountService,
    private router: Router,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      // close sidenav on routing
      this.usuarioMenu.close();
    });
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }
}
