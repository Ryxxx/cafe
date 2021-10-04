import * as dayjs from 'dayjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { ISessionPanier } from 'app/entities/session-panier/session-panier.model';

export interface IPanier {
  id?: number;
  quantite?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  produitId?: IProduit | null;
  sessionPanierIds?: ISessionPanier[] | null;
}

export class Panier implements IPanier {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public produitId?: IProduit | null,
    public sessionPanierIds?: ISessionPanier[] | null
  ) {}
}

export function getPanierIdentifier(panier: IPanier): number | undefined {
  return panier.id;
}
