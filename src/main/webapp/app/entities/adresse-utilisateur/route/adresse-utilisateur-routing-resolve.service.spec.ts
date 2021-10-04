jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdresseUtilisateur, AdresseUtilisateur } from '../adresse-utilisateur.model';
import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';

import { AdresseUtilisateurRoutingResolveService } from './adresse-utilisateur-routing-resolve.service';

describe('Service Tests', () => {
  describe('AdresseUtilisateur routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdresseUtilisateurRoutingResolveService;
    let service: AdresseUtilisateurService;
    let resultAdresseUtilisateur: IAdresseUtilisateur | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdresseUtilisateurRoutingResolveService);
      service = TestBed.inject(AdresseUtilisateurService);
      resultAdresseUtilisateur = undefined;
    });

    describe('resolve', () => {
      it('should return IAdresseUtilisateur returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdresseUtilisateur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdresseUtilisateur).toEqual({ id: 123 });
      });

      it('should return new IAdresseUtilisateur if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdresseUtilisateur = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdresseUtilisateur).toEqual(new AdresseUtilisateur());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AdresseUtilisateur })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdresseUtilisateur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdresseUtilisateur).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
