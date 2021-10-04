jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISessionPanier, SessionPanier } from '../session-panier.model';
import { SessionPanierService } from '../service/session-panier.service';

import { SessionPanierRoutingResolveService } from './session-panier-routing-resolve.service';

describe('Service Tests', () => {
  describe('SessionPanier routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SessionPanierRoutingResolveService;
    let service: SessionPanierService;
    let resultSessionPanier: ISessionPanier | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SessionPanierRoutingResolveService);
      service = TestBed.inject(SessionPanierService);
      resultSessionPanier = undefined;
    });

    describe('resolve', () => {
      it('should return ISessionPanier returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSessionPanier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSessionPanier).toEqual({ id: 123 });
      });

      it('should return new ISessionPanier if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSessionPanier = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSessionPanier).toEqual(new SessionPanier());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SessionPanier })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSessionPanier = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSessionPanier).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
