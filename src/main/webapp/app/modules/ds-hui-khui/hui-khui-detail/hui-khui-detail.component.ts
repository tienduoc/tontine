import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { flattenDeep, sumBy } from 'lodash';

import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Component({
  selector: 'jhi-hui-khui-detail',
  templateUrl: './hui-khui-detail.component.html',
  styleUrls: ['./hui-khui-detail.component.scss'],
})
export class HuiKhuiDetailComponent {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ds-hui-khui');

  data: any;

  oldFontSize!: string;

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.getDsHuiKhui();
  }

  ngOnDestroy(): void {
    const element = document.querySelector('.headerinfo');
    const headerinfoAddress = document.querySelector('.headerinfoAddress');

    if (element) {
      (element as HTMLElement).style.textAlign = 'center';
      (headerinfoAddress as HTMLElement).style.width = 'auto';
      (headerinfoAddress as HTMLElement).style.fontSize = this.oldFontSize;
    }
  }

  ngAfterViewInit() {
    const element = document.querySelector('.headerinfo');
    const headerinfoAddress = document.querySelector('.headerinfoAddress');

    if (element) {
      (element as HTMLElement).style.textAlign = 'left';
      (headerinfoAddress as HTMLElement).style.width = '150px';
      this.oldFontSize = (headerinfoAddress as HTMLElement).style.fontSize;
      (headerinfoAddress as HTMLElement).style.fontSize = '12px';
    }
  }

  getDsHuiKhui(): void {
    const id = this.activatedRoute.snapshot.params.id;

    this.http.get<any>(`${this.resourceUrl}/user/${id}`, { observe: 'response' }).subscribe(data => {
      this.data = data.body;
    });
  }

  tonSoTienBoTham(): number {
    return sumBy(flattenDeep(this.data?.dayHuis), 'soTienBoTham') || 0;
  }

  tonSoTienHot(): number {
    return sumBy(flattenDeep(this.data?.dayHuis), 'soTienHot') || 0;
  }

  tonSoTienDaDong(): number {
    return sumBy(flattenDeep(this.data?.dayHuis), 'soTienDong') || 0;
  }

  formatCurrentDate() {
    const now = new Date();
    const day = now.getDate();
    const month = now.getMonth() + 1; // Vì getMonth() trả về từ 0-11
    const year = now.getFullYear();
    const hours = now.getHours();
    const minutes = now.getMinutes();

    return `Ngày ${day} tháng ${month} năm ${year} - ${hours} giờ ${minutes} phút`;
  }
}
