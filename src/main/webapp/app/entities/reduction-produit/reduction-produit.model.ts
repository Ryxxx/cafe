import * as dayjs from 'dayjs';
import { IProduit } from 'app/entities/produit/produit.model';

export interface IReductionProduit {
  id?: number;
  nom?: string | null;
  description?: string | null;
  pourcentageReduction?: number | null;
  actif?: boolean | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  dateSuppression?: dayjs.Dayjs | null;
  produitIds?: IProduit[] | null;
}

export class ReductionProduit implements IReductionProduit {
  constructor(
    public id?: number,
    public nom?: string | null,
    public description?: string | null,
    public pourcentageReduction?: number | null,
    public actif?: boolean | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public dateSuppression?: dayjs.Dayjs | null,
    public produitIds?: IProduit[] | null
  ) {
    this.actif = this.actif ?? false;
  }
}

export function getReductionProduitIdentifier(reductionProduit: IReductionProduit): number | undefined {
  return reductionProduit.id;
}
