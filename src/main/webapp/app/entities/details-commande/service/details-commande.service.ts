import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetailsCommande, getDetailsCommandeIdentifier } from '../details-commande.model';

export type EntityResponseType = HttpResponse<IDetailsCommande>;
export type EntityArrayResponseType = HttpResponse<IDetailsCommande[]>;

@Injectable({ providedIn: 'root' })
export class DetailsCommandeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/details-commandes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detailsCommande: IDetailsCommande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailsCommande);
    return this.http
      .post<IDetailsCommande>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(detailsCommande: IDetailsCommande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailsCommande);
    return this.http
      .put<IDetailsCommande>(`${this.resourceUrl}/${getDetailsCommandeIdentifier(detailsCommande) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(detailsCommande: IDetailsCommande): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailsCommande);
    return this.http
      .patch<IDetailsCommande>(`${this.resourceUrl}/${getDetailsCommandeIdentifier(detailsCommande) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDetailsCommande>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDetailsCommande[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDetailsCommandeToCollectionIfMissing(
    detailsCommandeCollection: IDetailsCommande[],
    ...detailsCommandesToCheck: (IDetailsCommande | null | undefined)[]
  ): IDetailsCommande[] {
    const detailsCommandes: IDetailsCommande[] = detailsCommandesToCheck.filter(isPresent);
    if (detailsCommandes.length > 0) {
      const detailsCommandeCollectionIdentifiers = detailsCommandeCollection.map(
        detailsCommandeItem => getDetailsCommandeIdentifier(detailsCommandeItem)!
      );
      const detailsCommandesToAdd = detailsCommandes.filter(detailsCommandeItem => {
        const detailsCommandeIdentifier = getDetailsCommandeIdentifier(detailsCommandeItem);
        if (detailsCommandeIdentifier == null || detailsCommandeCollectionIdentifiers.includes(detailsCommandeIdentifier)) {
          return false;
        }
        detailsCommandeCollectionIdentifiers.push(detailsCommandeIdentifier);
        return true;
      });
      return [...detailsCommandesToAdd, ...detailsCommandeCollection];
    }
    return detailsCommandeCollection;
  }

  protected convertDateFromClient(detailsCommande: IDetailsCommande): IDetailsCommande {
    return Object.assign({}, detailsCommande, {
      dateCreation: detailsCommande.dateCreation?.isValid() ? detailsCommande.dateCreation.format(DATE_FORMAT) : undefined,
      dateModification: detailsCommande.dateModification?.isValid() ? detailsCommande.dateModification.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((detailsCommande: IDetailsCommande) => {
        detailsCommande.dateCreation = detailsCommande.dateCreation ? dayjs(detailsCommande.dateCreation) : undefined;
        detailsCommande.dateModification = detailsCommande.dateModification ? dayjs(detailsCommande.dateModification) : undefined;
      });
    }
    return res;
  }
}
