import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdresseUtilisateurComponent } from '../list/adresse-utilisateur.component';
import { AdresseUtilisateurDetailComponent } from '../detail/adresse-utilisateur-detail.component';
import { AdresseUtilisateurUpdateComponent } from '../update/adresse-utilisateur-update.component';
import { AdresseUtilisateurRoutingResolveService } from './adresse-utilisateur-routing-resolve.service';

const adresseUtilisateurRoute: Routes = [
  {
    path: '',
    component: AdresseUtilisateurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdresseUtilisateurDetailComponent,
    resolve: {
      adresseUtilisateur: AdresseUtilisateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdresseUtilisateurUpdateComponent,
    resolve: {
      adresseUtilisateur: AdresseUtilisateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdresseUtilisateurUpdateComponent,
    resolve: {
      adresseUtilisateur: AdresseUtilisateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(adresseUtilisateurRoute)],
  exports: [RouterModule],
})
export class AdresseUtilisateurRoutingModule {}
