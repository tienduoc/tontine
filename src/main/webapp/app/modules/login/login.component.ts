import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/modules/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { take } from 'rxjs';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;

  loginForm = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
  });

  constructor(private accountService: AccountService, private loginService: LoginService, private router: Router) {}

  ngOnInit(): void {
    this.isAuthenticated();
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  login(): void {
    const credentials = {
      ...this.loginForm.getRawValue(),
      rememberMe: false,
    };
    this.loginService.login(credentials).subscribe({
      next: () => {
        this.authenticationError = false;
        // if (!this.router.getCurrentNavigation()) {
        this.router.navigate(['/hui']);
        // }
      },
      error: () => (this.authenticationError = true),
    });
  }

  // if already authenticated then navigate to home page
  private isAuthenticated(): void {
    this.accountService
      .identity()
      .pipe(take(1))
      .subscribe(() => {
        if (this.accountService.isAuthenticated()) {
          this.router.navigate(['/home']);
        }
      });
  }
}
