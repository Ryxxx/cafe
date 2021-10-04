import { IUser } from 'app/entities/user/user.model';

export interface IAdresseUtilisateur {
  id?: number;
  adresseLigne1?: string | null;
  adresseLigne2?: string | null;
  ville?: string | null;
  codePostal?: string | null;
  pays?: string | null;
  telephone?: string | null;
  userId?: IUser | null;
}

export class AdresseUtilisateur implements IAdresseUtilisateur {
  constructor(
    public id?: number,
    public adresseLigne1?: string | null,
    public adresseLigne2?: string | null,
    public ville?: string | null,
    public codePostal?: string | null,
    public pays?: string | null,
    public telephone?: string | null,
    public userId?: IUser | null
  ) {}
}

export function getAdresseUtilisateurIdentifier(adresseUtilisateur: IAdresseUtilisateur): number | undefined {
  return adresseUtilisateur.id;
}
