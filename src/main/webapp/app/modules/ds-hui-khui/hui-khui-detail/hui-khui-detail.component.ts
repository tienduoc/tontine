import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { sortBy } from 'lodash';
import { MatDialog } from '@angular/material/dialog';

import { TinhTienPopupComponent } from 'app/components/tinh-tien-popup/tinh-tien-popup.component';
import { IHui } from '../../hui/hui.model';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-hui-khui-detail',
  templateUrl: './hui-khui-detail.component.html',
  styleUrls: ['./hui-khui-detail.component.scss'],
})
export class HuiKhuiDetailComponent {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ds-hui-khui');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    private activatedRoute: ActivatedRoute
  ) {
    this.getDsHuiKhui();
  }

  getDsHuiKhui(): void {
    const id = this.activatedRoute.snapshot.params.id;

    this.http.get<any>(`${this.resourceUrl}/user/${id}`, { observe: 'response' }).subscribe(data => {
      console.log(data);
    });
  }
}
