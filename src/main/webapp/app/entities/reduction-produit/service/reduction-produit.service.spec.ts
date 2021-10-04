import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IReductionProduit, ReductionProduit } from '../reduction-produit.model';

import { ReductionProduitService } from './reduction-produit.service';

describe('Service Tests', () => {
  describe('ReductionProduit Service', () => {
    let service: ReductionProduitService;
    let httpMock: HttpTestingController;
    let elemDefault: IReductionProduit;
    let expectedResult: IReductionProduit | IReductionProduit[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReductionProduitService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        description: 'AAAAAAA',
        pourcentageReduction: 0,
        actif: false,
        dateCreation: currentDate,
        dateModification: currentDate,
        dateSuppression: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
            dateSuppression: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ReductionProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
            dateSuppression: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
            dateSuppression: currentDate,
          },
          returnedFromService
        );

        service.create(new ReductionProduit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ReductionProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            description: 'BBBBBB',
            pourcentageReduction: 1,
            actif: true,
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
            dateSuppression: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
            dateSuppression: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ReductionProduit', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
            actif: true,
          },
          new ReductionProduit()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
            dateSuppression: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ReductionProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            description: 'BBBBBB',
            pourcentageReduction: 1,
            actif: true,
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
            dateSuppression: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
            dateSuppression: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ReductionProduit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReductionProduitToCollectionIfMissing', () => {
        it('should add a ReductionProduit to an empty array', () => {
          const reductionProduit: IReductionProduit = { id: 123 };
          expectedResult = service.addReductionProduitToCollectionIfMissing([], reductionProduit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reductionProduit);
        });

        it('should not add a ReductionProduit to an array that contains it', () => {
          const reductionProduit: IReductionProduit = { id: 123 };
          const reductionProduitCollection: IReductionProduit[] = [
            {
              ...reductionProduit,
            },
            { id: 456 },
          ];
          expectedResult = service.addReductionProduitToCollectionIfMissing(reductionProduitCollection, reductionProduit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ReductionProduit to an array that doesn't contain it", () => {
          const reductionProduit: IReductionProduit = { id: 123 };
          const reductionProduitCollection: IReductionProduit[] = [{ id: 456 }];
          expectedResult = service.addReductionProduitToCollectionIfMissing(reductionProduitCollection, reductionProduit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reductionProduit);
        });

        it('should add only unique ReductionProduit to an array', () => {
          const reductionProduitArray: IReductionProduit[] = [{ id: 123 }, { id: 456 }, { id: 55338 }];
          const reductionProduitCollection: IReductionProduit[] = [{ id: 123 }];
          expectedResult = service.addReductionProduitToCollectionIfMissing(reductionProduitCollection, ...reductionProduitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const reductionProduit: IReductionProduit = { id: 123 };
          const reductionProduit2: IReductionProduit = { id: 456 };
          expectedResult = service.addReductionProduitToCollectionIfMissing([], reductionProduit, reductionProduit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reductionProduit);
          expect(expectedResult).toContain(reductionProduit2);
        });

        it('should accept null and undefined values', () => {
          const reductionProduit: IReductionProduit = { id: 123 };
          expectedResult = service.addReductionProduitToCollectionIfMissing([], null, reductionProduit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reductionProduit);
        });

        it('should return initial array if no ReductionProduit is added', () => {
          const reductionProduitCollection: IReductionProduit[] = [{ id: 123 }];
          expectedResult = service.addReductionProduitToCollectionIfMissing(reductionProduitCollection, undefined, null);
          expect(expectedResult).toEqual(reductionProduitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
