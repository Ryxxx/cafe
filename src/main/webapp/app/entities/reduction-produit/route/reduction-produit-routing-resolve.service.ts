import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReductionProduit, ReductionProduit } from '../reduction-produit.model';
import { ReductionProduitService } from '../service/reduction-produit.service';

@Injectable({ providedIn: 'root' })
export class ReductionProduitRoutingResolveService implements Resolve<IReductionProduit> {
  constructor(protected service: ReductionProduitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReductionProduit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reductionProduit: HttpResponse<ReductionProduit>) => {
          if (reductionProduit.body) {
            return of(reductionProduit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReductionProduit());
  }
}
