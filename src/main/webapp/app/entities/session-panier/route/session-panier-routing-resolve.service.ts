import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISessionPanier, SessionPanier } from '../session-panier.model';
import { SessionPanierService } from '../service/session-panier.service';

@Injectable({ providedIn: 'root' })
export class SessionPanierRoutingResolveService implements Resolve<ISessionPanier> {
  constructor(protected service: SessionPanierService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISessionPanier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sessionPanier: HttpResponse<SessionPanier>) => {
          if (sessionPanier.body) {
            return of(sessionPanier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SessionPanier());
  }
}
