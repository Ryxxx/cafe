import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaiement, getPaiementIdentifier } from '../paiement.model';

export type EntityResponseType = HttpResponse<IPaiement>;
export type EntityArrayResponseType = HttpResponse<IPaiement[]>;

@Injectable({ providedIn: 'root' })
export class PaiementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paiement: IPaiement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiement);
    return this.http
      .post<IPaiement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paiement: IPaiement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiement);
    return this.http
      .put<IPaiement>(`${this.resourceUrl}/${getPaiementIdentifier(paiement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(paiement: IPaiement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiement);
    return this.http
      .patch<IPaiement>(`${this.resourceUrl}/${getPaiementIdentifier(paiement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaiement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaiement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPaiementToCollectionIfMissing(paiementCollection: IPaiement[], ...paiementsToCheck: (IPaiement | null | undefined)[]): IPaiement[] {
    const paiements: IPaiement[] = paiementsToCheck.filter(isPresent);
    if (paiements.length > 0) {
      const paiementCollectionIdentifiers = paiementCollection.map(paiementItem => getPaiementIdentifier(paiementItem)!);
      const paiementsToAdd = paiements.filter(paiementItem => {
        const paiementIdentifier = getPaiementIdentifier(paiementItem);
        if (paiementIdentifier == null || paiementCollectionIdentifiers.includes(paiementIdentifier)) {
          return false;
        }
        paiementCollectionIdentifiers.push(paiementIdentifier);
        return true;
      });
      return [...paiementsToAdd, ...paiementCollection];
    }
    return paiementCollection;
  }

  protected convertDateFromClient(paiement: IPaiement): IPaiement {
    return Object.assign({}, paiement, {
      dateCreation: paiement.dateCreation?.isValid() ? paiement.dateCreation.format(DATE_FORMAT) : undefined,
      dateModification: paiement.dateModification?.isValid() ? paiement.dateModification.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
      res.body.dateModification = res.body.dateModification ? dayjs(res.body.dateModification) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paiement: IPaiement) => {
        paiement.dateCreation = paiement.dateCreation ? dayjs(paiement.dateCreation) : undefined;
        paiement.dateModification = paiement.dateModification ? dayjs(paiement.dateModification) : undefined;
      });
    }
    return res;
  }
}
