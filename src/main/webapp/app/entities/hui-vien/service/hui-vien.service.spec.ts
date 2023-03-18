import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHuiVien } from '../hui-vien.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../hui-vien.test-samples';

import { HuiVienService } from './hui-vien.service';

const requireRestSample: IHuiVien = {
  ...sampleWithRequiredData,
};

describe('HuiVien Service', () => {
  let service: HuiVienService;
  let httpMock: HttpTestingController;
  let expectedResult: IHuiVien | IHuiVien[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HuiVienService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a HuiVien', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const huiVien = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(huiVien).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HuiVien', () => {
      const huiVien = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(huiVien).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HuiVien', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HuiVien', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HuiVien', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHuiVienToCollectionIfMissing', () => {
      it('should add a HuiVien to an empty array', () => {
        const huiVien: IHuiVien = sampleWithRequiredData;
        expectedResult = service.addHuiVienToCollectionIfMissing([], huiVien);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(huiVien);
      });

      it('should not add a HuiVien to an array that contains it', () => {
        const huiVien: IHuiVien = sampleWithRequiredData;
        const huiVienCollection: IHuiVien[] = [
          {
            ...huiVien,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHuiVienToCollectionIfMissing(huiVienCollection, huiVien);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HuiVien to an array that doesn't contain it", () => {
        const huiVien: IHuiVien = sampleWithRequiredData;
        const huiVienCollection: IHuiVien[] = [sampleWithPartialData];
        expectedResult = service.addHuiVienToCollectionIfMissing(huiVienCollection, huiVien);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(huiVien);
      });

      it('should add only unique HuiVien to an array', () => {
        const huiVienArray: IHuiVien[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const huiVienCollection: IHuiVien[] = [sampleWithRequiredData];
        expectedResult = service.addHuiVienToCollectionIfMissing(huiVienCollection, ...huiVienArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const huiVien: IHuiVien = sampleWithRequiredData;
        const huiVien2: IHuiVien = sampleWithPartialData;
        expectedResult = service.addHuiVienToCollectionIfMissing([], huiVien, huiVien2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(huiVien);
        expect(expectedResult).toContain(huiVien2);
      });

      it('should accept null and undefined values', () => {
        const huiVien: IHuiVien = sampleWithRequiredData;
        expectedResult = service.addHuiVienToCollectionIfMissing([], null, huiVien, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(huiVien);
      });

      it('should return initial array if no HuiVien is added', () => {
        const huiVienCollection: IHuiVien[] = [sampleWithRequiredData];
        expectedResult = service.addHuiVienToCollectionIfMissing(huiVienCollection, undefined, null);
        expect(expectedResult).toEqual(huiVienCollection);
      });
    });

    describe('compareHuiVien', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHuiVien(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHuiVien(entity1, entity2);
        const compareResult2 = service.compareHuiVien(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHuiVien(entity1, entity2);
        const compareResult2 = service.compareHuiVien(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHuiVien(entity1, entity2);
        const compareResult2 = service.compareHuiVien(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
