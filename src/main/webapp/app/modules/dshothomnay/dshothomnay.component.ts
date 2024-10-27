import { Component, OnInit } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { DsHomnayService } from './service/dshomnay.service';

@Component({
  selector: 'jhi-ds-hot-nay',
  templateUrl: './dshothomnay.component.html',
  styleUrls: ['./dshothomnay.component.css'],
})
export class DsHotHomNayComponent {
  thongkes: any;

  constructor(private dsHomnayService: DsHomnayService) {
    this.dsHomnayService.getThongKe(20241022).subscribe(data => {
      // console.log(data.body);
      this.thongkes = [
        {
          tenHuiVien: 'Mỹ',
          chiTiet: [
            {
              tenHui: 'Hụi tuần 1 triệu',
              huiHot: 1000000,
              huiSong: null,
              huiChet: null,
              conLai: 3000000,
            },
            {
              tenHui: 'Hụi tuần 1 triệu/16 phần',
              huiHot: 1000000,
              huiSong: 200000,
              huiChet: 1000000,
              conLai: 3000000,
            },
          ],
        },
        {
          tenHuiVien: 'Cô phương',
          chiTiet: [
            {
              tenHui: 'Hụi tuần 1 triệu',
              huiHot: 1000000,
              huiSong: null,
              huiChet: null,
              conLai: 3000000,
            },
            {
              tenHui: 'Hụi tuần 1 triệu/16 phần',
              huiHot: 1000000,
              huiSong: 200000,
              huiChet: 1000000,
              conLai: 3000000,
            },
          ],
        },
        {
          tenHuiVien: 'Duy',
          chiTiet: [
            {
              tenHui: 'Hụi tuần 1 triệu',
              huiHot: 1000000,
              huiSong: null,
              huiChet: null,
              conLai: 3000000,
            },
            {
              tenHui: 'Hụi tuần 1 triệu/16 phần',
              huiHot: 1000000,
              huiSong: 200000,
              huiChet: 1000000,
              conLai: 3000000,
            },
          ],
        },
      ];
    });
  }

  in(index: number): void {
    console.log('ádasdas', index);
  }
}
