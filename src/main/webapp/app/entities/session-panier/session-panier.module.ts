import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SessionPanierComponent } from './list/session-panier.component';
import { SessionPanierDetailComponent } from './detail/session-panier-detail.component';
import { SessionPanierUpdateComponent } from './update/session-panier-update.component';
import { SessionPanierDeleteDialogComponent } from './delete/session-panier-delete-dialog.component';
import { SessionPanierRoutingModule } from './route/session-panier-routing.module';

@NgModule({
  imports: [SharedModule, SessionPanierRoutingModule],
  declarations: [SessionPanierComponent, SessionPanierDetailComponent, SessionPanierUpdateComponent, SessionPanierDeleteDialogComponent],
  entryComponents: [SessionPanierDeleteDialogComponent],
})
export class SessionPanierModule {}
