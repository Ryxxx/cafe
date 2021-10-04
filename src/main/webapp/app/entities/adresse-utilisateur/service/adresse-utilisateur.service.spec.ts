import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAdresseUtilisateur, AdresseUtilisateur } from '../adresse-utilisateur.model';

import { AdresseUtilisateurService } from './adresse-utilisateur.service';

describe('Service Tests', () => {
  describe('AdresseUtilisateur Service', () => {
    let service: AdresseUtilisateurService;
    let httpMock: HttpTestingController;
    let elemDefault: IAdresseUtilisateur;
    let expectedResult: IAdresseUtilisateur | IAdresseUtilisateur[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AdresseUtilisateurService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        adresseLigne1: 'AAAAAAA',
        adresseLigne2: 'AAAAAAA',
        ville: 'AAAAAAA',
        codePostal: 'AAAAAAA',
        pays: 'AAAAAAA',
        telephone: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a AdresseUtilisateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AdresseUtilisateur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AdresseUtilisateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            adresseLigne1: 'BBBBBB',
            adresseLigne2: 'BBBBBB',
            ville: 'BBBBBB',
            codePostal: 'BBBBBB',
            pays: 'BBBBBB',
            telephone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AdresseUtilisateur', () => {
        const patchObject = Object.assign(
          {
            adresseLigne1: 'BBBBBB',
            adresseLigne2: 'BBBBBB',
            ville: 'BBBBBB',
          },
          new AdresseUtilisateur()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AdresseUtilisateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            adresseLigne1: 'BBBBBB',
            adresseLigne2: 'BBBBBB',
            ville: 'BBBBBB',
            codePostal: 'BBBBBB',
            pays: 'BBBBBB',
            telephone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a AdresseUtilisateur', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAdresseUtilisateurToCollectionIfMissing', () => {
        it('should add a AdresseUtilisateur to an empty array', () => {
          const adresseUtilisateur: IAdresseUtilisateur = { id: 123 };
          expectedResult = service.addAdresseUtilisateurToCollectionIfMissing([], adresseUtilisateur);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(adresseUtilisateur);
        });

        it('should not add a AdresseUtilisateur to an array that contains it', () => {
          const adresseUtilisateur: IAdresseUtilisateur = { id: 123 };
          const adresseUtilisateurCollection: IAdresseUtilisateur[] = [
            {
              ...adresseUtilisateur,
            },
            { id: 456 },
          ];
          expectedResult = service.addAdresseUtilisateurToCollectionIfMissing(adresseUtilisateurCollection, adresseUtilisateur);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AdresseUtilisateur to an array that doesn't contain it", () => {
          const adresseUtilisateur: IAdresseUtilisateur = { id: 123 };
          const adresseUtilisateurCollection: IAdresseUtilisateur[] = [{ id: 456 }];
          expectedResult = service.addAdresseUtilisateurToCollectionIfMissing(adresseUtilisateurCollection, adresseUtilisateur);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(adresseUtilisateur);
        });

        it('should add only unique AdresseUtilisateur to an array', () => {
          const adresseUtilisateurArray: IAdresseUtilisateur[] = [{ id: 123 }, { id: 456 }, { id: 76127 }];
          const adresseUtilisateurCollection: IAdresseUtilisateur[] = [{ id: 123 }];
          expectedResult = service.addAdresseUtilisateurToCollectionIfMissing(adresseUtilisateurCollection, ...adresseUtilisateurArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const adresseUtilisateur: IAdresseUtilisateur = { id: 123 };
          const adresseUtilisateur2: IAdresseUtilisateur = { id: 456 };
          expectedResult = service.addAdresseUtilisateurToCollectionIfMissing([], adresseUtilisateur, adresseUtilisateur2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(adresseUtilisateur);
          expect(expectedResult).toContain(adresseUtilisateur2);
        });

        it('should accept null and undefined values', () => {
          const adresseUtilisateur: IAdresseUtilisateur = { id: 123 };
          expectedResult = service.addAdresseUtilisateurToCollectionIfMissing([], null, adresseUtilisateur, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(adresseUtilisateur);
        });

        it('should return initial array if no AdresseUtilisateur is added', () => {
          const adresseUtilisateurCollection: IAdresseUtilisateur[] = [{ id: 123 }];
          expectedResult = service.addAdresseUtilisateurToCollectionIfMissing(adresseUtilisateurCollection, undefined, null);
          expect(expectedResult).toEqual(adresseUtilisateurCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
