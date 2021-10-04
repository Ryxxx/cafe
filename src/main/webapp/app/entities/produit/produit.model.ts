import * as dayjs from 'dayjs';
import { ICategorieProduit } from 'app/entities/categorie-produit/categorie-produit.model';
import { IStockProduit } from 'app/entities/stock-produit/stock-produit.model';
import { IReductionProduit } from 'app/entities/reduction-produit/reduction-produit.model';

export interface IProduit {
  id?: number;
  nom?: string | null;
  description?: string | null;
  sku?: string | null;
  prix?: number | null;
  reductionId?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  dateSuppression?: dayjs.Dayjs | null;
  categorieProduit?: ICategorieProduit | null;
  stockProduit?: IStockProduit | null;
  reductionProduit?: IReductionProduit | null;
}

export class Produit implements IProduit {
  constructor(
    public id?: number,
    public nom?: string | null,
    public description?: string | null,
    public sku?: string | null,
    public prix?: number | null,
    public reductionId?: number | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateModification?: dayjs.Dayjs | null,
    public dateSuppression?: dayjs.Dayjs | null,
    public categorieProduit?: ICategorieProduit | null,
    public stockProduit?: IStockProduit | null,
    public reductionProduit?: IReductionProduit | null
  ) {}
}

export function getProduitIdentifier(produit: IProduit): number | undefined {
  return produit.id;
}
