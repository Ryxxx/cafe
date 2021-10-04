import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StockProduitComponent } from '../list/stock-produit.component';
import { StockProduitDetailComponent } from '../detail/stock-produit-detail.component';
import { StockProduitUpdateComponent } from '../update/stock-produit-update.component';
import { StockProduitRoutingResolveService } from './stock-produit-routing-resolve.service';

const stockProduitRoute: Routes = [
  {
    path: '',
    component: StockProduitComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StockProduitDetailComponent,
    resolve: {
      stockProduit: StockProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StockProduitUpdateComponent,
    resolve: {
      stockProduit: StockProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StockProduitUpdateComponent,
    resolve: {
      stockProduit: StockProduitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(stockProduitRoute)],
  exports: [RouterModule],
})
export class StockProduitRoutingModule {}
