import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStockProduit, getStockProduitIdentifier } from '../stock-produit.model';

export type EntityResponseType = HttpResponse<IStockProduit>;
export type EntityArrayResponseType = HttpResponse<IStockProduit[]>;

@Injectable({ providedIn: 'root' })
export class StockProduitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stock-produits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(stockProduit: IStockProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockProduit);
    return this.http
      .post<IStockProduit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(stockProduit: IStockProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockProduit);
    return this.http
      .put<IStockProduit>(`${this.resourceUrl}/${getStockProduitIdentifier(stockProduit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(stockProduit: IStockProduit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(stockProduit);
    return this.http
      .patch<IStockProduit>(`${this.resourceUrl}/${getStockProduitIdentifier(stockProduit) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IStockProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IStockProduit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStockProduitToCollectionIfMissing(
    stockProduitCollection: IStockProduit[],
    ...stockProduitsToCheck: (IStockProduit | null | undefined)[]
  ): IStockProduit[] {
    const stockProduits: IStockProduit[] = stockProduitsToCheck.filter(isPresent);
    if (stockProduits.length > 0) {
      const stockProduitCollectionIdentifiers = stockProduitCollection.map(
        stockProduitItem => getStockProduitIdentifier(stockProduitItem)!
      );
      const stockProduitsToAdd = stockProduits.filter(stockProduitItem => {
        const stockProduitIdentifier = getStockProduitIdentifier(stockProduitItem);
        if (stockProduitIdentifier == null || stockProduitCollectionIdentifiers.includes(stockProduitIdentifier)) {
          return false;
        }
        stockProduitCollectionIdentifiers.push(stockProduitIdentifier);
        return true;
      });
      return [...stockProduitsToAdd, ...stockProduitCollection];
    }
    return stockProduitCollection;
  }

  protected convertDateFromClient(stockProduit: IStockProduit): IStockProduit {
    return Object.assign({}, stockProduit, {
      dateCreation: stockProduit.dateCreation?.isValid() ? stockProduit.dateCreation.format(DATE_FORMAT) : undefined,
      dateModification: stockProduit.dateModification?.isValid() ? stockProduit.dateModification.format(DATE_FORMAT) : undefined,
      dateSuppression: stockProduit.dateSuppression?.isValid() ? stockProduit.dateSuppression.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((stockProduit: IStockProduit) => {
        stockProduit.dateCreation = stockProduit.dateCreation ? dayjs(stockProduit.dateCreation) : undefined;
        stockProduit.dateModification = stockProduit.dateModification ? dayjs(stockProduit.dateModification) : undefined;
        stockProduit.dateSuppression = stockProduit.dateSuppression ? dayjs(stockProduit.dateSuppression) : undefined;
      });
    }
    return res;
  }
}
