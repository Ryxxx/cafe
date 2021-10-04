import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStockProduit } from '../stock-produit.model';

@Component({
  selector: 'jhi-stock-produit-detail',
  templateUrl: './stock-produit-detail.component.html',
})
export class StockProduitDetailComponent implements OnInit {
  stockProduit: IStockProduit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockProduit }) => {
      this.stockProduit = stockProduit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
