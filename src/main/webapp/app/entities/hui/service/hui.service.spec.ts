import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHui } from '../hui.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../hui.test-samples';

import { HuiService, RestHui } from './hui.service';

const requireRestSample: RestHui = {
  ...sampleWithRequiredData,
  ngayTao: sampleWithRequiredData.ngayTao?.format(DATE_FORMAT),
};

describe('Hui Service', () => {
  let service: HuiService;
  let httpMock: HttpTestingController;
  let expectedResult: IHui | IHui[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HuiService);
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

    it('should create a Hui', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const hui = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hui).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hui', () => {
      const hui = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hui).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Hui', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Hui', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Hui', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHuiToCollectionIfMissing', () => {
      it('should add a Hui to an empty array', () => {
        const hui: IHui = sampleWithRequiredData;
        expectedResult = service.addHuiToCollectionIfMissing([], hui);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hui);
      });

      it('should not add a Hui to an array that contains it', () => {
        const hui: IHui = sampleWithRequiredData;
        const huiCollection: IHui[] = [
          {
            ...hui,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHuiToCollectionIfMissing(huiCollection, hui);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hui to an array that doesn't contain it", () => {
        const hui: IHui = sampleWithRequiredData;
        const huiCollection: IHui[] = [sampleWithPartialData];
        expectedResult = service.addHuiToCollectionIfMissing(huiCollection, hui);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hui);
      });

      it('should add only unique Hui to an array', () => {
        const huiArray: IHui[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const huiCollection: IHui[] = [sampleWithRequiredData];
        expectedResult = service.addHuiToCollectionIfMissing(huiCollection, ...huiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hui: IHui = sampleWithRequiredData;
        const hui2: IHui = sampleWithPartialData;
        expectedResult = service.addHuiToCollectionIfMissing([], hui, hui2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hui);
        expect(expectedResult).toContain(hui2);
      });

      it('should accept null and undefined values', () => {
        const hui: IHui = sampleWithRequiredData;
        expectedResult = service.addHuiToCollectionIfMissing([], null, hui, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hui);
      });

      it('should return initial array if no Hui is added', () => {
        const huiCollection: IHui[] = [sampleWithRequiredData];
        expectedResult = service.addHuiToCollectionIfMissing(huiCollection, undefined, null);
        expect(expectedResult).toEqual(huiCollection);
      });
    });

    describe('compareHui', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHui(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHui(entity1, entity2);
        const compareResult2 = service.compareHui(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHui(entity1, entity2);
        const compareResult2 = service.compareHui(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHui(entity1, entity2);
        const compareResult2 = service.compareHui(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
