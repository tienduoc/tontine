import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HuiDetailComponent } from './hui-detail.component';

describe('Hui Management Detail Component', () => {
  let comp: HuiDetailComponent;
  let fixture: ComponentFixture<HuiDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HuiDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ hui: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HuiDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HuiDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hui on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.hui).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
