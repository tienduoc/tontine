import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChiTietHuiFormService } from './chi-tiet-hui-form.service';
import { ChiTietHuiService } from '../service/chi-tiet-hui.service';
import { IChiTietHui } from '../chi-tiet-hui.model';
import { IHui } from 'app/entities/hui/hui.model';
import { HuiService } from 'app/entities/hui/service/hui.service';
import { IHuiVien } from 'app/entities/hui-vien/hui-vien.model';
import { HuiVienService } from 'app/entities/hui-vien/service/hui-vien.service';

import { ChiTietHuiUpdateComponent } from './chi-tiet-hui-update.component';

describe('ChiTietHui Management Update Component', () => {
  let comp: ChiTietHuiUpdateComponent;
  let fixture: ComponentFixture<ChiTietHuiUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chiTietHuiFormService: ChiTietHuiFormService;
  let chiTietHuiService: ChiTietHuiService;
  let huiService: HuiService;
  let huiVienService: HuiVienService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChiTietHuiUpdateComponent],
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
      .overrideTemplate(ChiTietHuiUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChiTietHuiUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chiTietHuiFormService = TestBed.inject(ChiTietHuiFormService);
    chiTietHuiService = TestBed.inject(ChiTietHuiService);
    huiService = TestBed.inject(HuiService);
    huiVienService = TestBed.inject(HuiVienService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Hui query and add missing value', () => {
      const chiTietHui: IChiTietHui = { id: 456 };
      const hui: IHui = { id: 38815 };
      chiTietHui.hui = hui;

      const huiCollection: IHui[] = [{ id: 18045 }];
      jest.spyOn(huiService, 'query').mockReturnValue(of(new HttpResponse({ body: huiCollection })));
      const additionalHuis = [hui];
      const expectedCollection: IHui[] = [...additionalHuis, ...huiCollection];
      jest.spyOn(huiService, 'addHuiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chiTietHui });
      comp.ngOnInit();

      expect(huiService.query).toHaveBeenCalled();
      expect(huiService.addHuiToCollectionIfMissing).toHaveBeenCalledWith(huiCollection, ...additionalHuis.map(expect.objectContaining));
      expect(comp.huisSharedCollection).toEqual(expectedCollection);
    });

    it('Should call HuiVien query and add missing value', () => {
      const chiTietHui: IChiTietHui = { id: 456 };
      const huiVien: IHuiVien = { id: 6768 };
      chiTietHui.huiVien = huiVien;

      const huiVienCollection: IHuiVien[] = [{ id: 32125 }];
      jest.spyOn(huiVienService, 'query').mockReturnValue(of(new HttpResponse({ body: huiVienCollection })));
      const additionalHuiViens = [huiVien];
      const expectedCollection: IHuiVien[] = [...additionalHuiViens, ...huiVienCollection];
      jest.spyOn(huiVienService, 'addHuiVienToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chiTietHui });
      comp.ngOnInit();

      expect(huiVienService.query).toHaveBeenCalled();
      expect(huiVienService.addHuiVienToCollectionIfMissing).toHaveBeenCalledWith(
        huiVienCollection,
        ...additionalHuiViens.map(expect.objectContaining)
      );
      expect(comp.huiViensSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const chiTietHui: IChiTietHui = { id: 456 };
      const hui: IHui = { id: 21113 };
      chiTietHui.hui = hui;
      const huiVien: IHuiVien = { id: 34856 };
      chiTietHui.huiVien = huiVien;

      activatedRoute.data = of({ chiTietHui });
      comp.ngOnInit();

      expect(comp.huisSharedCollection).toContain(hui);
      expect(comp.huiViensSharedCollection).toContain(huiVien);
      expect(comp.chiTietHui).toEqual(chiTietHui);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChiTietHui>>();
      const chiTietHui = { id: 123 };
      jest.spyOn(chiTietHuiFormService, 'getChiTietHui').mockReturnValue(chiTietHui);
      jest.spyOn(chiTietHuiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chiTietHui });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chiTietHui }));
      saveSubject.complete();

      // THEN
      expect(chiTietHuiFormService.getChiTietHui).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chiTietHuiService.update).toHaveBeenCalledWith(expect.objectContaining(chiTietHui));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChiTietHui>>();
      const chiTietHui = { id: 123 };
      jest.spyOn(chiTietHuiFormService, 'getChiTietHui').mockReturnValue({ id: null });
      jest.spyOn(chiTietHuiService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chiTietHui: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chiTietHui }));
      saveSubject.complete();

      // THEN
      expect(chiTietHuiFormService.getChiTietHui).toHaveBeenCalled();
      expect(chiTietHuiService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChiTietHui>>();
      const chiTietHui = { id: 123 };
      jest.spyOn(chiTietHuiService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chiTietHui });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chiTietHuiService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareHui', () => {
      it('Should forward to huiService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(huiService, 'compareHui');
        comp.compareHui(entity, entity2);
        expect(huiService.compareHui).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareHuiVien', () => {
      it('Should forward to huiVienService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(huiVienService, 'compareHuiVien');
        comp.compareHuiVien(entity, entity2);
        expect(huiVienService.compareHuiVien).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
