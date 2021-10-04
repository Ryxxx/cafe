import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPaiement, Paiement } from '../paiement.model';

import { PaiementService } from './paiement.service';

describe('Service Tests', () => {
  describe('Paiement Service', () => {
    let service: PaiementService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaiement;
    let expectedResult: IPaiement | IPaiement[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PaiementService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        quantite: 0,
        provider: 'AAAAAAA',
        statut: 'AAAAAAA',
        dateCreation: currentDate,
        dateModification: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Paiement', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
          },
          returnedFromService
        );

        service.create(new Paiement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Paiement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
            provider: 'BBBBBB',
            statut: 'BBBBBB',
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Paiement', () => {
        const patchObject = Object.assign(
          {
            provider: 'BBBBBB',
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
          },
          new Paiement()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Paiement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quantite: 1,
            provider: 'BBBBBB',
            statut: 'BBBBBB',
            dateCreation: currentDate.format(DATE_FORMAT),
            dateModification: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
            dateModification: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Paiement', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPaiementToCollectionIfMissing', () => {
        it('should add a Paiement to an empty array', () => {
          const paiement: IPaiement = { id: 123 };
          expectedResult = service.addPaiementToCollectionIfMissing([], paiement);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paiement);
        });

        it('should not add a Paiement to an array that contains it', () => {
          const paiement: IPaiement = { id: 123 };
          const paiementCollection: IPaiement[] = [
            {
              ...paiement,
            },
            { id: 456 },
          ];
          expectedResult = service.addPaiementToCollectionIfMissing(paiementCollection, paiement);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Paiement to an array that doesn't contain it", () => {
          const paiement: IPaiement = { id: 123 };
          const paiementCollection: IPaiement[] = [{ id: 456 }];
          expectedResult = service.addPaiementToCollectionIfMissing(paiementCollection, paiement);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paiement);
        });

        it('should add only unique Paiement to an array', () => {
          const paiementArray: IPaiement[] = [{ id: 123 }, { id: 456 }, { id: 69327 }];
          const paiementCollection: IPaiement[] = [{ id: 123 }];
          expectedResult = service.addPaiementToCollectionIfMissing(paiementCollection, ...paiementArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const paiement: IPaiement = { id: 123 };
          const paiement2: IPaiement = { id: 456 };
          expectedResult = service.addPaiementToCollectionIfMissing([], paiement, paiement2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(paiement);
          expect(expectedResult).toContain(paiement2);
        });

        it('should accept null and undefined values', () => {
          const paiement: IPaiement = { id: 123 };
          expectedResult = service.addPaiementToCollectionIfMissing([], null, paiement, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(paiement);
        });

        it('should return initial array if no Paiement is added', () => {
          const paiementCollection: IPaiement[] = [{ id: 123 }];
          expectedResult = service.addPaiementToCollectionIfMissing(paiementCollection, undefined, null);
          expect(expectedResult).toEqual(paiementCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
