import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IPanier } from 'app/entities/panier/panier.model';

export interface ISessionPanier {
  id?: number;
  total?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  userId?: IUser | null;
  panier?: IPanier | null;
}

export class SessionPanier implements ISessionPanier {
  constructor(
    public id?: number,
    public total?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public userId?: IUser | null,
    public panier?: IPanier | null
  ) {}
}

export function getSessionPanierIdentifier(sessionPanier: ISessionPanier): number | undefined {
  return sessionPanier.id;
}
