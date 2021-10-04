import * as dayjs from 'dayjs';

export interface IPaiement {
  id?: number;
  quantite?: number | null;
  provider?: string | null;
  statut?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
}

export class Paiement implements IPaiement {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public provider?: string | null,
    public statut?: string | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null
  ) {}
}

export function getPaiementIdentifier(paiement: IPaiement): number | undefined {
  return paiement.id;
}
