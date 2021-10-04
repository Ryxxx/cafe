import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDetailsCommande, DetailsCommande } from '../details-commande.model';

import { DetailsCommandeService } from './details-commande.service';

describe('Service Tests', () => {
  describe('DetailsCommande Service', () => {
    let service: DetailsCommandeService;
    let httpMock: HttpTestingController;
    let elemDefault: IDetailsCommande;
    let expectedResult: IDetailsCommande | IDetailsCommande[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DetailsCommandeService);
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

      it('should create a DetailsCommande', () => {
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

        service.create(new DetailsCommande()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DetailsCommande', () => {
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

      it('should partial update a DetailsCommande', () => {
        const patchObject = Object.assign({}, new DetailsCommande());

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

      it('should return a list of DetailsCommande', () => {
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

      it('should delete a DetailsCommande', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDetailsCommandeToCollectionIfMissing', () => {
        it('should add a DetailsCommande to an empty array', () => {
          const detailsCommande: IDetailsCommande = { id: 123 };
          expectedResult = service.addDetailsCommandeToCollectionIfMissing([], detailsCommande);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detailsCommande);
        });

        it('should not add a DetailsCommande to an array that contains it', () => {
          const detailsCommande: IDetailsCommande = { id: 123 };
          const detailsCommandeCollection: IDetailsCommande[] = [
            {
              ...detailsCommande,
            },
            { id: 456 },
          ];
          expectedResult = service.addDetailsCommandeToCollectionIfMissing(detailsCommandeCollection, detailsCommande);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DetailsCommande to an array that doesn't contain it", () => {
          const detailsCommande: IDetailsCommande = { id: 123 };
          const detailsCommandeCollection: IDetailsCommande[] = [{ id: 456 }];
          expectedResult = service.addDetailsCommandeToCollectionIfMissing(detailsCommandeCollection, detailsCommande);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detailsCommande);
        });

        it('should add only unique DetailsCommande to an array', () => {
          const detailsCommandeArray: IDetailsCommande[] = [{ id: 123 }, { id: 456 }, { id: 35370 }];
          const detailsCommandeCollection: IDetailsCommande[] = [{ id: 123 }];
          expectedResult = service.addDetailsCommandeToCollectionIfMissing(detailsCommandeCollection, ...detailsCommandeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const detailsCommande: IDetailsCommande = { id: 123 };
          const detailsCommande2: IDetailsCommande = { id: 456 };
          expectedResult = service.addDetailsCommandeToCollectionIfMissing([], detailsCommande, detailsCommande2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(detailsCommande);
          expect(expectedResult).toContain(detailsCommande2);
        });

        it('should accept null and undefined values', () => {
          const detailsCommande: IDetailsCommande = { id: 123 };
          expectedResult = service.addDetailsCommandeToCollectionIfMissing([], null, detailsCommande, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(detailsCommande);
        });

        it('should return initial array if no DetailsCommande is added', () => {
          const detailsCommandeCollection: IDetailsCommande[] = [{ id: 123 }];
          expectedResult = service.addDetailsCommandeToCollectionIfMissing(detailsCommandeCollection, undefined, null);
          expect(expectedResult).toEqual(detailsCommandeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
