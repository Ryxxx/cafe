import * as dayjs from 'dayjs';
import { IProduit } from 'app/entities/produit/produit.model';

export interface IStockProduit {
  id?: number;
  quantite?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  dateSuppression?: dayjs.Dayjs | null;
  produitIds?: IProduit[] | null;
}

export class StockProduit implements IStockProduit {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public dateSuppression?: dayjs.Dayjs | null,
    public produitIds?: IProduit[] | null
  ) {}
}

export function getStockProduitIdentifier(stockProduit: IStockProduit): number | undefined {
  return stockProduit.id;
}
