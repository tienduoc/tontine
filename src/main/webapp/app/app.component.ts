import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from './modules/login/login.service';

@Component({
  selector: 'app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  showFiller = false;
  routes = [
    { path: 'accounts', component: null, label: 'Accounts' },
    { path: 'contacts', component: null, label: 'Contacts' },
    { path: 'activities', component: null, label: 'Activities' },
  ];

  currentUser$ = this.accountService.getAuthenticationState();

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private loginService: LoginService
  ) {}

  ngOnInit(): void {
    // this.accountService.identity().subscribe();
    // this.router.events.subscribe(event => {
    //   if (event instanceof NavigationEnd) {
    //     this.updateTitle();
    //   }
    // });
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  // private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
  //   const title: string = routeSnapshot.data['pageTitle'] ?? '';
  //   if (routeSnapshot.firstChild) {
  //     return this.getPageTitle(routeSnapshot.firstChild) || title;
  //   }
  //   return title;
  // }

  // private updateTitle(): void {
  //   let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
  //   if (!pageTitle) {
  //     pageTitle = 'Tontine';
  //   }
  //   this.titleService.setTitle(pageTitle);
  // }
}
