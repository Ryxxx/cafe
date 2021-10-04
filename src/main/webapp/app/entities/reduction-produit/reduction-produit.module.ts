import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReductionProduitComponent } from './list/reduction-produit.component';
import { ReductionProduitDetailComponent } from './detail/reduction-produit-detail.component';
import { ReductionProduitUpdateComponent } from './update/reduction-produit-update.component';
import { ReductionProduitDeleteDialogComponent } from './delete/reduction-produit-delete-dialog.component';
import { ReductionProduitRoutingModule } from './route/reduction-produit-routing.module';

@NgModule({
  imports: [SharedModule, ReductionProduitRoutingModule],
  declarations: [
    ReductionProduitComponent,
    ReductionProduitDetailComponent,
    ReductionProduitUpdateComponent,
    ReductionProduitDeleteDialogComponent,
  ],
  entryComponents: [ReductionProduitDeleteDialogComponent],
})
export class ReductionProduitModule {}
