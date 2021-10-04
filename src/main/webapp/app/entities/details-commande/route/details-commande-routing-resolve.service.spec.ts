jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDetailsCommande, DetailsCommande } from '../details-commande.model';
import { DetailsCommandeService } from '../service/details-commande.service';

import { DetailsCommandeRoutingResolveService } from './details-commande-routing-resolve.service';

describe('Service Tests', () => {
  describe('DetailsCommande routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DetailsCommandeRoutingResolveService;
    let service: DetailsCommandeService;
    let resultDetailsCommande: IDetailsCommande | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DetailsCommandeRoutingResolveService);
      service = TestBed.inject(DetailsCommandeService);
      resultDetailsCommande = undefined;
    });

    describe('resolve', () => {
      it('should return IDetailsCommande returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDetailsCommande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDetailsCommande).toEqual({ id: 123 });
      });

      it('should return new IDetailsCommande if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDetailsCommande = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDetailsCommande).toEqual(new DetailsCommande());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DetailsCommande })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDetailsCommande = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDetailsCommande).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
