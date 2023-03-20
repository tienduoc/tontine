import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HuiVienDetailComponent } from './hui-vien-detail.component';

describe('HuiVien Management Detail Component', () => {
  let comp: HuiVienDetailComponent;
  let fixture: ComponentFixture<HuiVienDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HuiVienDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ huiVien: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HuiVienDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HuiVienDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load huiVien on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.huiVien).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
