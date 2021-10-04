import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReductionProduit, getReductionProduitIdentifier } from '../reduction-produit.model';

export type EntityResponseType = HttpResponse<IReductionProduit>;
export type EntityArrayResponseType = HttpResponse<IReductionProduit[]>;

@Injectable({ providedIn: 'root' })
export class ReductionProduitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reduction-produits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reductionProduit: IReductionProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reductionProduit);
    return this.http
      .post<IReductionProduit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(reductionProduit: IReductionProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reductionProduit);
    return this.http
      .put<IReductionProduit>(`${this.resourceUrl}/${getReductionProduitIdentifier(reductionProduit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(reductionProduit: IReductionProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(reductionProduit);
    return this.http
      .patch<IReductionProduit>(`${this.resourceUrl}/${getReductionProduitIdentifier(reductionProduit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReductionProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReductionProduit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReductionProduitToCollectionIfMissing(
    reductionProduitCollection: IReductionProduit[],
    ...reductionProduitsToCheck: (IReductionProduit | null | undefined)[]
  ): IReductionProduit[] {
    const reductionProduits: IReductionProduit[] = reductionProduitsToCheck.filter(isPresent);
    if (reductionProduits.length > 0) {
      const reductionProduitCollectionIdentifiers = reductionProduitCollection.map(
        reductionProduitItem => getReductionProduitIdentifier(reductionProduitItem)!
      );
      const reductionProduitsToAdd = reductionProduits.filter(reductionProduitItem => {
        const reductionProduitIdentifier = getReductionProduitIdentifier(reductionProduitItem);
        if (reductionProduitIdentifier == null || reductionProduitCollectionIdentifiers.includes(reductionProduitIdentifier)) {
          return false;
        }
        reductionProduitCollectionIdentifiers.push(reductionProduitIdentifier);
        return true;
      });
      return [...reductionProduitsToAdd, ...reductionProduitCollection];
    }
    return reductionProduitCollection;
  }

  protected convertDateFromClient(reductionProduit: IReductionProduit): IReductionProduit {
    return Object.assign({}, reductionProduit, {
      dateCreation: reductionProduit.dateCreation?.isValid() ? reductionProduit.dateCreation.format(DATE_FORMAT) : undefined,
      dateModification: reductionProduit.dateModification?.isValid() ? reductionProduit.dateModification.format(DATE_FORMAT) : undefined,
      dateSuppression: reductionProduit.dateSuppression?.isValid() ? reductionProduit.dateSuppression.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
      res.body.dateModification = res.body.dateModification ? dayjs(res.body.dateModification) : undefined;
      res.body.dateSuppression = res.body.dateSuppression ? dayjs(res.body.dateSuppression) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((reductionProduit: IReductionProduit) => {
        reductionProduit.dateCreation = reductionProduit.dateCreation ? dayjs(reductionProduit.dateCreation) : undefined;
        reductionProduit.dateModification = reductionProduit.dateModification ? dayjs(reductionProduit.dateModification) : undefined;
        reductionProduit.dateSuppression = reductionProduit.dateSuppression ? dayjs(reductionProduit.dateSuppression) : undefined;
      });
    }
    return res;
  }
}
