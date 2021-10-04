import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStockProduit, StockProduit } from '../stock-produit.model';
import { StockProduitService } from '../service/stock-produit.service';

@Injectable({ providedIn: 'root' })
export class StockProduitRoutingResolveService implements Resolve<IStockProduit> {
  constructor(protected service: StockProduitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStockProduit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((stockProduit: HttpResponse<StockProduit>) => {
          if (stockProduit.body) {
            return of(stockProduit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StockProduit());
  }
}
