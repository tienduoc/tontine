import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  selectedDate: string = '';

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  goBack(): void {
    this.router.navigate(['../../'], { relativeTo: this.activatedRoute });
  }

  ngOnInit(): void {
    this.getSelectedDate();
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

  getSelectedDate(): void {
    const savedDate = localStorage.getItem('selectedDate');

    if (savedDate) {
      const year = savedDate.substring(0, 4);
      const month = Number(savedDate.substring(4, 6));
      const day = Number(savedDate.substring(6, 8));

      this.selectedDate = `Ngày ${day} tháng ${month} năm ${year}`;
    } else {
      this.selectedDate = 'Không có ngày nào được chọn';
    }
  }

  getDsHuiKhui(): void {
    const id = this.activatedRoute.snapshot.params.userId;
    const savedDate = localStorage.getItem('selectedDate');

    this.http.get<any>(`${this.resourceUrl}/user/${id}?date=${savedDate}`, { observe: 'response' })
      .subscribe(data => {
        this.data = data.body;
      });
  }

  tongSoTienHot(): number {
    return sumBy(flattenDeep(this.data?.dayHuis), 'soTienHot') || 0;
  }

  tongSoTienDong(): number {
    return sumBy(flattenDeep(this.data?.dayHuis), 'soTienDong') || 0;
  }

  tongSoTienPhaiDong(): number {
    return this.tongSoTienDong() - this.tongSoTienHot();
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
