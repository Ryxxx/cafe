import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISessionPanier } from '../session-panier.model';

@Component({
  selector: 'jhi-session-panier-detail',
  templateUrl: './session-panier-detail.component.html',
})
export class SessionPanierDetailComponent implements OnInit {
  sessionPanier: ISessionPanier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionPanier }) => {
      this.sessionPanier = sessionPanier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
