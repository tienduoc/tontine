import { Component } from '@angular/core';
import { Account } from '../../core/auth/account.model';
import { VERSION } from '../../app.constants';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
})
export class FooterComponent {
  account: Account | null = null;
  version = '';

  constructor() {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }
}
