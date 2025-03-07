import { Component, OnInit, ViewChild } from '@angular/core';
import { DsHuiKhuiService } from '../service/ds-hui-khui.service';

@Component({
  selector: 'jhi-ds-hui-khui',
  templateUrl: './ds-hui-khui.component.html',
  styleUrls: ['./ds-hui-khui.component.css'],
})
export class DsHuiKhuiComponent implements OnInit {
  dsHuiKhui: any;

  @ViewChild('screen', { static: true }) screen: any;

  constructor(private dsHuiKhuiService: DsHuiKhuiService) {}

  ngOnInit(): void {
    this.getDsHuiKhui().subscribe(data => (this.dsHuiKhui = data?.body || []));
  }

  private getDsHuiKhui() {
    return this.dsHuiKhuiService.getDsHuiKhui();
  }
}
