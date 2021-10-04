import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DetailsCommandeComponent } from './list/details-commande.component';
import { DetailsCommandeDetailComponent } from './detail/details-commande-detail.component';
import { DetailsCommandeUpdateComponent } from './update/details-commande-update.component';
import { DetailsCommandeDeleteDialogComponent } from './delete/details-commande-delete-dialog.component';
import { DetailsCommandeRoutingModule } from './route/details-commande-routing.module';

@NgModule({
  imports: [SharedModule, DetailsCommandeRoutingModule],
  declarations: [
    DetailsCommandeComponent,
    DetailsCommandeDetailComponent,
    DetailsCommandeUpdateComponent,
    DetailsCommandeDeleteDialogComponent,
  ],
  entryComponents: [DetailsCommandeDeleteDialogComponent],
})
export class DetailsCommandeModule {}
