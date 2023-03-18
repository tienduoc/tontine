import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ChiTietHuiDetailComponent } from './chi-tiet-hui-detail.component';

describe('ChiTietHui Management Detail Component', () => {
  let comp: ChiTietHuiDetailComponent;
  let fixture: ComponentFixture<ChiTietHuiDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChiTietHuiDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ chiTietHui: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ChiTietHuiDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ChiTietHuiDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load chiTietHui on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.chiTietHui).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
