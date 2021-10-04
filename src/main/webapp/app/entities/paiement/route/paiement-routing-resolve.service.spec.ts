jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaiement, Paiement } from '../paiement.model';
import { PaiementService } from '../service/paiement.service';

import { PaiementRoutingResolveService } from './paiement-routing-resolve.service';

describe('Service Tests', () => {
  describe('Paiement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PaiementRoutingResolveService;
    let service: PaiementService;
    let resultPaiement: IPaiement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PaiementRoutingResolveService);
      service = TestBed.inject(PaiementService);
      resultPaiement = undefined;
    });

    describe('resolve', () => {
      it('should return IPaiement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaiement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaiement).toEqual({ id: 123 });
      });

      it('should return new IPaiement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaiement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPaiement).toEqual(new Paiement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Paiement })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaiement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaiement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
