import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISessionPanier, getSessionPanierIdentifier } from '../session-panier.model';

export type EntityResponseType = HttpResponse<ISessionPanier>;
export type EntityArrayResponseType = HttpResponse<ISessionPanier[]>;

@Injectable({ providedIn: 'root' })
export class SessionPanierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/session-paniers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sessionPanier: ISessionPanier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionPanier);
    return this.http
      .post<ISessionPanier>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sessionPanier: ISessionPanier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionPanier);
    return this.http
      .put<ISessionPanier>(`${this.resourceUrl}/${getSessionPanierIdentifier(sessionPanier) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sessionPanier: ISessionPanier): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sessionPanier);
    return this.http
      .patch<ISessionPanier>(`${this.resourceUrl}/${getSessionPanierIdentifier(sessionPanier) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISessionPanier>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISessionPanier[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSessionPanierToCollectionIfMissing(
    sessionPanierCollection: ISessionPanier[],
    ...sessionPaniersToCheck: (ISessionPanier | null | undefined)[]
  ): ISessionPanier[] {
    const sessionPaniers: ISessionPanier[] = sessionPaniersToCheck.filter(isPresent);
    if (sessionPaniers.length > 0) {
      const sessionPanierCollectionIdentifiers = sessionPanierCollection.map(
        sessionPanierItem => getSessionPanierIdentifier(sessionPanierItem)!
      );
      const sessionPaniersToAdd = sessionPaniers.filter(sessionPanierItem => {
        const sessionPanierIdentifier = getSessionPanierIdentifier(sessionPanierItem);
        if (sessionPanierIdentifier == null || sessionPanierCollectionIdentifiers.includes(sessionPanierIdentifier)) {
          return false;
        }
        sessionPanierCollectionIdentifiers.push(sessionPanierIdentifier);
        return true;
      });
      return [...sessionPaniersToAdd, ...sessionPanierCollection];
    }
    return sessionPanierCollection;
  }

  protected convertDateFromClient(sessionPanier: ISessionPanier): ISessionPanier {
    return Object.assign({}, sessionPanier, {
      dateCreation: sessionPanier.dateCreation?.isValid() ? sessionPanier.dateCreation.format(DATE_FORMAT) : undefined,
      dateModification: sessionPanier.dateModification?.isValid() ? sessionPanier.dateModification.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((sessionPanier: ISessionPanier) => {
        sessionPanier.dateCreation = sessionPanier.dateCreation ? dayjs(sessionPanier.dateCreation) : undefined;
        sessionPanier.dateModification = sessionPanier.dateModification ? dayjs(sessionPanier.dateModification) : undefined;
      });
    }
    return res;
  }
}
