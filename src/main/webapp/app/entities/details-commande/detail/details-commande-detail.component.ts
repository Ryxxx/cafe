import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDetailsCommande } from '../details-commande.model';

@Component({
  selector: 'jhi-details-commande-detail',
  templateUrl: './details-commande-detail.component.html',
})
export class DetailsCommandeDetailComponent implements OnInit {
  detailsCommande: IDetailsCommande | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailsCommande }) => {
      this.detailsCommande = detailsCommande;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
