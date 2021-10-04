import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategorieProduit, getCategorieProduitIdentifier } from '../categorie-produit.model';

export type EntityResponseType = HttpResponse<ICategorieProduit>;
export type EntityArrayResponseType = HttpResponse<ICategorieProduit[]>;

@Injectable({ providedIn: 'root' })
export class CategorieProduitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categorie-produits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categorieProduit: ICategorieProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categorieProduit);
    return this.http
      .post<ICategorieProduit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(categorieProduit: ICategorieProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categorieProduit);
    return this.http
      .put<ICategorieProduit>(`${this.resourceUrl}/${getCategorieProduitIdentifier(categorieProduit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(categorieProduit: ICategorieProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(categorieProduit);
    return this.http
      .patch<ICategorieProduit>(`${this.resourceUrl}/${getCategorieProduitIdentifier(categorieProduit) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICategorieProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICategorieProduit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCategorieProduitToCollectionIfMissing(
    categorieProduitCollection: ICategorieProduit[],
    ...categorieProduitsToCheck: (ICategorieProduit | null | undefined)[]
  ): ICategorieProduit[] {
    const categorieProduits: ICategorieProduit[] = categorieProduitsToCheck.filter(isPresent);
    if (categorieProduits.length > 0) {
      const categorieProduitCollectionIdentifiers = categorieProduitCollection.map(
        categorieProduitItem => getCategorieProduitIdentifier(categorieProduitItem)!
      );
      const categorieProduitsToAdd = categorieProduits.filter(categorieProduitItem => {
        const categorieProduitIdentifier = getCategorieProduitIdentifier(categorieProduitItem);
        if (categorieProduitIdentifier == null || categorieProduitCollectionIdentifiers.includes(categorieProduitIdentifier)) {
          return false;
        }
        categorieProduitCollectionIdentifiers.push(categorieProduitIdentifier);
        return true;
      });
      return [...categorieProduitsToAdd, ...categorieProduitCollection];
    }
    return categorieProduitCollection;
  }

  protected convertDateFromClient(categorieProduit: ICategorieProduit): ICategorieProduit {
    return Object.assign({}, categorieProduit, {
      dateCreation: categorieProduit.dateCreation?.isValid() ? categorieProduit.dateCreation.format(DATE_FORMAT) : undefined,
      dateModification: categorieProduit.dateModification?.isValid() ? categorieProduit.dateModification.format(DATE_FORMAT) : undefined,
      dateSuppression: categorieProduit.dateSuppression?.isValid() ? categorieProduit.dateSuppression.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((categorieProduit: ICategorieProduit) => {
        categorieProduit.dateCreation = categorieProduit.dateCreation ? dayjs(categorieProduit.dateCreation) : undefined;
        categorieProduit.dateModification = categorieProduit.dateModification ? dayjs(categorieProduit.dateModification) : undefined;
        categorieProduit.dateSuppression = categorieProduit.dateSuppression ? dayjs(categorieProduit.dateSuppression) : undefined;
      });
    }
    return res;
  }
}
