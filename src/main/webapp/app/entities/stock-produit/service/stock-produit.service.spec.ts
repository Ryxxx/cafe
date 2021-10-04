import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IStockProduit, StockProduit } from '../stock-produit.model';

import { StockProduitService } from './stock-produit.service';

describe('Service Tests', () => {
  describe('StockProduit Service', () => {
    let service: StockProduitService;
    let httpMock: HttpTestingController;
    let elemDefault: IStockProduit;
    let expectedResult: IStockProduit | IStockProduit[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StockProduitService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        quantite: 0,
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

      it('should create a StockProduit', () => {
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

        service.create(new StockProduit()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StockProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
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

      it('should partial update a StockProduit', () => {
        const patchObject = Object.assign(
          {
            quantite: 1,
            dateModification: currentDate.format(DATE_FORMAT),
          },
          new StockProduit()
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

      it('should return a list of StockProduit', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
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

      it('should delete a StockProduit', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStockProduitToCollectionIfMissing', () => {
        it('should add a StockProduit to an empty array', () => {
          const stockProduit: IStockProduit = { id: 123 };
          expectedResult = service.addStockProduitToCollectionIfMissing([], stockProduit);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(stockProduit);
        });

        it('should not add a StockProduit to an array that contains it', () => {
          const stockProduit: IStockProduit = { id: 123 };
          const stockProduitCollection: IStockProduit[] = [
            {
              ...stockProduit,
            },
            { id: 456 },
          ];
          expectedResult = service.addStockProduitToCollectionIfMissing(stockProduitCollection, stockProduit);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a StockProduit to an array that doesn't contain it", () => {
          const stockProduit: IStockProduit = { id: 123 };
          const stockProduitCollection: IStockProduit[] = [{ id: 456 }];
          expectedResult = service.addStockProduitToCollectionIfMissing(stockProduitCollection, stockProduit);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(stockProduit);
        });

        it('should add only unique StockProduit to an array', () => {
          const stockProduitArray: IStockProduit[] = [{ id: 123 }, { id: 456 }, { id: 36456 }];
          const stockProduitCollection: IStockProduit[] = [{ id: 123 }];
          expectedResult = service.addStockProduitToCollectionIfMissing(stockProduitCollection, ...stockProduitArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const stockProduit: IStockProduit = { id: 123 };
          const stockProduit2: IStockProduit = { id: 456 };
          expectedResult = service.addStockProduitToCollectionIfMissing([], stockProduit, stockProduit2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(stockProduit);
          expect(expectedResult).toContain(stockProduit2);
        });

        it('should accept null and undefined values', () => {
          const stockProduit: IStockProduit = { id: 123 };
          expectedResult = service.addStockProduitToCollectionIfMissing([], null, stockProduit, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(stockProduit);
        });

        it('should return initial array if no StockProduit is added', () => {
          const stockProduitCollection: IStockProduit[] = [{ id: 123 }];
          expectedResult = service.addStockProduitToCollectionIfMissing(stockProduitCollection, undefined, null);
          expect(expectedResult).toEqual(stockProduitCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
