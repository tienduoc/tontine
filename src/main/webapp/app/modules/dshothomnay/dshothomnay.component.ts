import { Component, ViewChild } from '@angular/core';
import { DsHomNayService } from './service/ds-hom-nay.service';
import { FormControl } from '@angular/forms';
import { NgxCaptureService } from 'ngx-capture';
import { finalize, startWith, switchMap, tap } from 'rxjs';

@Component({
  selector: 'jhi-ds-hot-nay',
  templateUrl: './dshothomnay.component.html',
  styleUrls: ['./dshothomnay.component.css'],
})
export class DsHotHomNayComponent {
  thongkes: any;
  todayControl: FormControl<any>;
  tableCaptureId!: number;
  isCapture = false;

  @ViewChild('screen', { static: true }) screen: any;

  formatDate(): string {
    const date = new Date(this.todayControl.value);
    const day = date.getDate();
    const month = date.getMonth() + 1; // Months are zero-indexed
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
  }

  ngAfterViewInit(): void {
    (document.querySelector('.timeResult') as any).style.display = 'none';
  }

  ngOnInit(): void {
    this.todayControl.valueChanges
      .pipe(
        startWith(new Date()),
        switchMap(dateSelected => {
          const day = dateSelected.getDate().toString().padStart(2, '0');
          const month = (dateSelected.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-indexed
          const year = dateSelected.getFullYear();

          const stringDate = `${year}${month}${day}`;
          return this.getThongKe(Number(stringDate));
        })
      )
      .subscribe(data => {
        this.thongkes = (data?.body || [])
          // Sort by name
          .sort((a: any, b: any) => a.tenHuiVien.localeCompare(b.tenHuiVien))
          .map((tk: any) => {
            tk.chiTiet = tk.chiTiet.sort((a: any, b: any) => {
              // Display order: HuiHot > HuiSong > HuiChet
              const h1 = a.huiHot !== 0 ? 0 : a.huiSong !== 0 ? 1 : 2;
              const h2 = b.huiHot !== 0 ? 0 : b.huiSong !== 0 ? 1 : 2;
              return h1 - h2;
            });
            return tk;
          });
      });
  }

  constructor(private dsHomNayService: DsHomNayService, private captureService: NgxCaptureService) {
    this.todayControl = new FormControl(new Date().toISOString());
  }

  getThongKe(dateSelect: number) {
    return this.dsHomNayService.getThongKe(dateSelect);
  }

  in(curIndex: number): void {
    (document.querySelector('.pickerField') as any).style.display = 'none';
    (document.querySelector('.timeResult') as any).style.display = 'block';

    const brElements = document.querySelectorAll('br');
    document.querySelectorAll('br').forEach(br => {
      (br as HTMLElement).style.display = 'none';
    });

    (this.thongkes || []).forEach((_: any, index: number) => {
      if (curIndex !== index) {
        (document.querySelector(`.tb${index}`) as any).style.display = 'none';
      }
    });

    this.captureService
      .getImage(this.screen.nativeElement, true)
      .pipe(
        tap(img => {
          const link = document.createElement('a');

          document.body.appendChild(link);

          link.setAttribute('href', img);
          link.setAttribute('download', 'danhsachhothomnay');
          link.click();
        }),
        finalize(() => {
          (document.querySelector('.pickerField') as any).style.display = 'none';
          (document.querySelector('.timeResult') as any).style.display = 'none';

          (this.thongkes || []).forEach((_: any, index: number) => {
            (document.querySelector(`.tb${index}`) as any).style.display = 'table';
          });

          brElements.forEach(br => {
            (br as HTMLElement).style.display = '';
          });
        })
      )
      .subscribe();
  }
}