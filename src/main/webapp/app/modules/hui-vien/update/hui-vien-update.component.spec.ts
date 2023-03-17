import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HuiVienFormService } from './hui-vien-form.service';
import { HuiVienService } from '../service/hui-vien.service';
import { IHuiVien } from '../hui-vien.model';

import { HuiVienUpdateComponent } from './hui-vien-update.component';

describe('HuiVien Management Update Component', () => {
  let comp: HuiVienUpdateComponent;
  let fixture: ComponentFixture<HuiVienUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let huiVienFormService: HuiVienFormService;
  let huiVienService: HuiVienService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HuiVienUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(HuiVienUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HuiVienUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    huiVienFormService = TestBed.inject(HuiVienFormService);
    huiVienService = TestBed.inject(HuiVienService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const huiVien: IHuiVien = { id: 456 };

      activatedRoute.data = of({ huiVien });
      comp.ngOnInit();

      expect(comp.huiVien).toEqual(huiVien);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHuiVien>>();
      const huiVien = { id: 123 };
      jest.spyOn(huiVienFormService, 'getHuiVien').mockReturnValue(huiVien);
      jest.spyOn(huiVienService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ huiVien });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: huiVien }));
      saveSubject.complete();

      // THEN
      expect(huiVienFormService.getHuiVien).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(huiVienService.update).toHaveBeenCalledWith(expect.objectContaining(huiVien));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHuiVien>>();
      const huiVien = { id: 123 };
      jest.spyOn(huiVienFormService, 'getHuiVien').mockReturnValue({ id: null });
      jest.spyOn(huiVienService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ huiVien: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: huiVien }));
      saveSubject.complete();

      // THEN
      expect(huiVienFormService.getHuiVien).toHaveBeenCalled();
      expect(huiVienService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHuiVien>>();
      const huiVien = { id: 123 };
      jest.spyOn(huiVienService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ huiVien });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(huiVienService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
