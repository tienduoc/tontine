import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';

import { Router } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from './modules/login/login.service';
import { NgxCaptureService } from 'ngx-capture';
import { tap } from 'rxjs';
import { HuiVienService } from './modules/hui-vien/service/hui-vien.service';

@Component({
  selector: 'app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  currentUser$ = this.accountService.getAuthenticationState();
  @ViewChild('drawer', { static: false }) usuarioMenu!: MatSidenav;
  @ViewChild('screen', { static: true }) screen: any;

  constructor(
    private matSidenav: MatSidenav,
    private accountService: AccountService,
    private router: Router,
    private loginService: LoginService,
    private captureService: NgxCaptureService,
    public service: HuiVienService
  ) {}

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  capture() {
    this.captureService
      .getImage(this.screen.nativeElement, true)
      .pipe(
        tap(img => {
          const link = document.createElement('a');

          document.body.appendChild(link);

          link.setAttribute('href', img);
          link.setAttribute('download', 'huiDetail');
          link.click();
        })
      )
      .subscribe();
  }
}
