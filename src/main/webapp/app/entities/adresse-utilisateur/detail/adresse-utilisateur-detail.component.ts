import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdresseUtilisateur } from '../adresse-utilisateur.model';

@Component({
  selector: 'jhi-adresse-utilisateur-detail',
  templateUrl: './adresse-utilisateur-detail.component.html',
})
export class AdresseUtilisateurDetailComponent implements OnInit {
  adresseUtilisateur: IAdresseUtilisateur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adresseUtilisateur }) => {
      this.adresseUtilisateur = adresseUtilisateur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
