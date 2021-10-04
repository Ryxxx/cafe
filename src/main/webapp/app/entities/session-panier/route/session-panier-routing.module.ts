import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SessionPanierComponent } from '../list/session-panier.component';
import { SessionPanierDetailComponent } from '../detail/session-panier-detail.component';
import { SessionPanierUpdateComponent } from '../update/session-panier-update.component';
import { SessionPanierRoutingResolveService } from './session-panier-routing-resolve.service';

const sessionPanierRoute: Routes = [
  {
    path: '',
    component: SessionPanierComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SessionPanierDetailComponent,
    resolve: {
      sessionPanier: SessionPanierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SessionPanierUpdateComponent,
    resolve: {
      sessionPanier: SessionPanierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SessionPanierUpdateComponent,
    resolve: {
      sessionPanier: SessionPanierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sessionPanierRoute)],
  exports: [RouterModule],
})
export class SessionPanierRoutingModule {}
