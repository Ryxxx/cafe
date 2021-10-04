jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStockProduit, StockProduit } from '../stock-produit.model';
import { StockProduitService } from '../service/stock-produit.service';

import { StockProduitRoutingResolveService } from './stock-produit-routing-resolve.service';

describe('Service Tests', () => {
  describe('StockProduit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StockProduitRoutingResolveService;
    let service: StockProduitService;
    let resultStockProduit: IStockProduit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StockProduitRoutingResolveService);
      service = TestBed.inject(StockProduitService);
      resultStockProduit = undefined;
    });

    describe('resolve', () => {
      it('should return IStockProduit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStockProduit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStockProduit).toEqual({ id: 123 });
      });

      it('should return new IStockProduit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStockProduit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStockProduit).toEqual(new StockProduit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as StockProduit })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStockProduit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStockProduit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
