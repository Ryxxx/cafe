import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdresseUtilisateur, getAdresseUtilisateurIdentifier } from '../adresse-utilisateur.model';

export type EntityResponseType = HttpResponse<IAdresseUtilisateur>;
export type EntityArrayResponseType = HttpResponse<IAdresseUtilisateur[]>;

@Injectable({ providedIn: 'root' })
export class AdresseUtilisateurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/adresse-utilisateurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(adresseUtilisateur: IAdresseUtilisateur): Observable<EntityResponseType> {
    return this.http.post<IAdresseUtilisateur>(this.resourceUrl, adresseUtilisateur, { observe: 'response' });
  }

  update(adresseUtilisateur: IAdresseUtilisateur): Observable<EntityResponseType> {
    return this.http.put<IAdresseUtilisateur>(
      `${this.resourceUrl}/${getAdresseUtilisateurIdentifier(adresseUtilisateur) as number}`,
      adresseUtilisateur,
      { observe: 'response' }
    );
  }

  partialUpdate(adresseUtilisateur: IAdresseUtilisateur): Observable<EntityResponseType> {
    return this.http.patch<IAdresseUtilisateur>(
      `${this.resourceUrl}/${getAdresseUtilisateurIdentifier(adresseUtilisateur) as number}`,
      adresseUtilisateur,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdresseUtilisateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdresseUtilisateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdresseUtilisateurToCollectionIfMissing(
    adresseUtilisateurCollection: IAdresseUtilisateur[],
    ...adresseUtilisateursToCheck: (IAdresseUtilisateur | null | undefined)[]
  ): IAdresseUtilisateur[] {
    const adresseUtilisateurs: IAdresseUtilisateur[] = adresseUtilisateursToCheck.filter(isPresent);
    if (adresseUtilisateurs.length > 0) {
      const adresseUtilisateurCollectionIdentifiers = adresseUtilisateurCollection.map(
        adresseUtilisateurItem => getAdresseUtilisateurIdentifier(adresseUtilisateurItem)!
      );
      const adresseUtilisateursToAdd = adresseUtilisateurs.filter(adresseUtilisateurItem => {
        const adresseUtilisateurIdentifier = getAdresseUtilisateurIdentifier(adresseUtilisateurItem);
        if (adresseUtilisateurIdentifier == null || adresseUtilisateurCollectionIdentifiers.includes(adresseUtilisateurIdentifier)) {
          return false;
        }
        adresseUtilisateurCollectionIdentifiers.push(adresseUtilisateurIdentifier);
        return true;
      });
      return [...adresseUtilisateursToAdd, ...adresseUtilisateurCollection];
    }
    return adresseUtilisateurCollection;
  }
}
