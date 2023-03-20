import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HuiFormService } from './hui-form.service';
import { HuiService } from '../service/hui.service';
import { IHui } from '../hui.model';

import { HuiUpdateComponent } from './hui-update.component';

describe('Hui Management Update Component', () => {
  let comp: HuiUpdateComponent;
  let fixture: ComponentFixture<HuiUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let huiFormService: HuiFormService;
  let huiService: HuiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HuiUpdateComponent],
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
      .overrideTemplate(HuiUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HuiUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    huiFormService = TestBed.inject(HuiFormService);
    huiService = TestBed.inject(HuiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const hui: IHui = { id: 456 };

      activatedRoute.data = of({ hui });
      comp.ngOnInit();

      expect(comp.hui).toEqual(hui);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHui>>();
      const hui = { id: 123 };
      jest.spyOn(huiFormService, 'getHui').mockReturnValue(hui);
      jest.spyOn(huiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hui });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hui }));
      saveSubject.complete();

      // THEN
      expect(huiFormService.getHui).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(huiService.update).toHaveBeenCalledWith(expect.objectContaining(hui));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHui>>();
      const hui = { id: 123 };
      jest.spyOn(huiFormService, 'getHui').mockReturnValue({ id: null });
      jest.spyOn(huiService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hui: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: hui }));
      saveSubject.complete();

      // THEN
      expect(huiFormService.getHui).toHaveBeenCalled();
      expect(huiService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHui>>();
      const hui = { id: 123 };
      jest.spyOn(huiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ hui });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(huiService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
