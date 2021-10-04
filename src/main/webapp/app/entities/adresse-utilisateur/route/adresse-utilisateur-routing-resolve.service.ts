import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdresseUtilisateur, AdresseUtilisateur } from '../adresse-utilisateur.model';
import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';

@Injectable({ providedIn: 'root' })
export class AdresseUtilisateurRoutingResolveService implements Resolve<IAdresseUtilisateur> {
  constructor(protected service: AdresseUtilisateurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdresseUtilisateur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((adresseUtilisateur: HttpResponse<AdresseUtilisateur>) => {
          if (adresseUtilisateur.body) {
            return of(adresseUtilisateur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdresseUtilisateur());
  }
}
