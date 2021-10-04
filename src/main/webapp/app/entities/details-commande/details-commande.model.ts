import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IPaiement } from 'app/entities/paiement/paiement.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface IDetailsCommande {
  id?: number;
  total?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  userId?: IUser | null;
  paimentId?: IPaiement | null;
  commande?: ICommande | null;
}

export class DetailsCommande implements IDetailsCommande {
  constructor(
    public id?: number,
    public total?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public userId?: IUser | null,
    public paimentId?: IPaiement | null,
    public commande?: ICommande | null
  ) {}
}

export function getDetailsCommandeIdentifier(detailsCommande: IDetailsCommande): number | undefined {
  return detailsCommande.id;
}
