import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISessionPanier, SessionPanier } from '../session-panier.model';

import { SessionPanierService } from './session-panier.service';

describe('Service Tests', () => {
  describe('SessionPanier Service', () => {
    let service: SessionPanierService;
    let httpMock: HttpTestingController;
    let elemDefault: ISessionPanier;
    let expectedResult: ISessionPanier | ISessionPanier[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SessionPanierService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        total: 0,
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

      it('should create a SessionPanier', () => {
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

        service.create(new SessionPanier()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SessionPanier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            total: 1,
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

      it('should partial update a SessionPanier', () => {
        const patchObject = Object.assign(
          {
            dateModification: currentDate.format(DATE_FORMAT),
          },
          new SessionPanier()
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

      it('should return a list of SessionPanier', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            total: 1,
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

      it('should delete a SessionPanier', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSessionPanierToCollectionIfMissing', () => {
        it('should add a SessionPanier to an empty array', () => {
          const sessionPanier: ISessionPanier = { id: 123 };
          expectedResult = service.addSessionPanierToCollectionIfMissing([], sessionPanier);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sessionPanier);
        });

        it('should not add a SessionPanier to an array that contains it', () => {
          const sessionPanier: ISessionPanier = { id: 123 };
          const sessionPanierCollection: ISessionPanier[] = [
            {
              ...sessionPanier,
            },
            { id: 456 },
          ];
          expectedResult = service.addSessionPanierToCollectionIfMissing(sessionPanierCollection, sessionPanier);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SessionPanier to an array that doesn't contain it", () => {
          const sessionPanier: ISessionPanier = { id: 123 };
          const sessionPanierCollection: ISessionPanier[] = [{ id: 456 }];
          expectedResult = service.addSessionPanierToCollectionIfMissing(sessionPanierCollection, sessionPanier);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sessionPanier);
        });

        it('should add only unique SessionPanier to an array', () => {
          const sessionPanierArray: ISessionPanier[] = [{ id: 123 }, { id: 456 }, { id: 11208 }];
          const sessionPanierCollection: ISessionPanier[] = [{ id: 123 }];
          expectedResult = service.addSessionPanierToCollectionIfMissing(sessionPanierCollection, ...sessionPanierArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sessionPanier: ISessionPanier = { id: 123 };
          const sessionPanier2: ISessionPanier = { id: 456 };
          expectedResult = service.addSessionPanierToCollectionIfMissing([], sessionPanier, sessionPanier2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sessionPanier);
          expect(expectedResult).toContain(sessionPanier2);
        });

        it('should accept null and undefined values', () => {
          const sessionPanier: ISessionPanier = { id: 123 };
          expectedResult = service.addSessionPanierToCollectionIfMissing([], null, sessionPanier, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sessionPanier);
        });

        it('should return initial array if no SessionPanier is added', () => {
          const sessionPanierCollection: ISessionPanier[] = [{ id: 123 }];
          expectedResult = service.addSessionPanierToCollectionIfMissing(sessionPanierCollection, undefined, null);
          expect(expectedResult).toEqual(sessionPanierCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
