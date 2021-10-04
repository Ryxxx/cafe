import * as dayjs from 'dayjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { IDetailsCommande } from 'app/entities/details-commande/details-commande.model';

export interface ICommande {
  id?: number;
  detailsCommandeId?: number | null;
  quantite?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  produitId?: IProduit | null;
  detailsCommandeIds?: IDetailsCommande[] | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public detailsCommandeId?: number | null,
    public quantite?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public produitId?: IProduit | null,
    public detailsCommandeIds?: IDetailsCommande[] | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): number | undefined {
  return commande.id;
}
