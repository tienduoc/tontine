
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';

import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'app',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  currentUser$ = this.accountService.getAuthenticationState();

  constructor(
    private accountService: AccountService, 
    private titleService: Title, 
    private router: Router
  ) {}

  ngOnInit(): void {
    // this.accountService.identity().subscribe();

    // this.router.events.subscribe(event => {
    //   if (event instanceof NavigationEnd) {
    //     this.updateTitle();
    //   }
    // });
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
