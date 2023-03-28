import { Component } from '@angular/core';
import { pluck } from 'rxjs';
import { TinhTienService } from './service/tinh-tien-popup.service';

@Component({
  selector: 'jhi-tinh-tien-popup',
  templateUrl: './tinh-tien-popup.component.html',
  styleUrls: ['./tinh-tien-popup.component.scss'],
})
export class TinhTienPopupComponnet {
  constructor(private tinhTienService: TinhTienService) {}
  tinhtien: any;

  ngOnInit(): void {
    this.tinhTienService
      .find(1057)
      .pipe(pluck('body'))
      .subscribe(data => {
        console.log(data);
        this.tinhtien = data;
      });
  }
}
