import * as dayjs from 'dayjs';
import { IProduit } from 'app/entities/produit/produit.model';

export interface ICategorieProduit {
  id?: number;
  nom?: string | null;
  description?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  dateSuppression?: dayjs.Dayjs | null;
  produitIds?: IProduit[] | null;
}

export class CategorieProduit implements ICategorieProduit {
  constructor(
    public id?: number,
    public nom?: string | null,
    public description?: string | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public dateSuppression?: dayjs.Dayjs | null,
    public produitIds?: IProduit[] | null
  ) {}
}

export function getCategorieProduitIdentifier(categorieProduit: ICategorieProduit): number | undefined {
  return categorieProduit.id;
}
