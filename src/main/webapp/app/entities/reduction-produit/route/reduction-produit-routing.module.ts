import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReductionProduitComponent } from '../list/reduction-produit.component';
import { ReductionProduitDetailComponent } from '../detail/reduction-produit-detail.component';
import { ReductionProduitUpdateComponent } from '../update/reduction-produit-update.component';
import { ReductionProduitRoutingResolveService } from './reduction-produit-routing-resolve.service';

const reductionProduitRoute: Routes = [
  {
    path: '',
    component: ReductionProduitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReductionProduitDetailComponent,
    resolve: {
      reductionProduit: ReductionProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReductionProduitUpdateComponent,
    resolve: {
      reductionProduit: ReductionProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReductionProduitUpdateComponent,
    resolve: {
      reductionProduit: ReductionProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reductionProduitRoute)],
  exports: [RouterModule],
})
export class ReductionProduitRoutingModule {}
