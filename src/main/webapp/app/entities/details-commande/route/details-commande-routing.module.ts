import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DetailsCommandeComponent } from '../list/details-commande.component';
import { DetailsCommandeDetailComponent } from '../detail/details-commande-detail.component';
import { DetailsCommandeUpdateComponent } from '../update/details-commande-update.component';
import { DetailsCommandeRoutingResolveService } from './details-commande-routing-resolve.service';

const detailsCommandeRoute: Routes = [
  {
    path: '',
    component: DetailsCommandeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetailsCommandeDetailComponent,
    resolve: {
      detailsCommande: DetailsCommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DetailsCommandeUpdateComponent,
    resolve: {
      detailsCommande: DetailsCommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DetailsCommandeUpdateComponent,
    resolve: {
      detailsCommande: DetailsCommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(detailsCommandeRoute)],
  exports: [RouterModule],
})
export class DetailsCommandeRoutingModule {}
