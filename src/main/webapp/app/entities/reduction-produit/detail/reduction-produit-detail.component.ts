import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReductionProduit } from '../reduction-produit.model';

@Component({
  selector: 'jhi-reduction-produit-detail',
  templateUrl: './reduction-produit-detail.component.html',
})
export class ReductionProduitDetailComponent implements OnInit {
  reductionProduit: IReductionProduit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reductionProduit }) => {
      this.reductionProduit = reductionProduit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
