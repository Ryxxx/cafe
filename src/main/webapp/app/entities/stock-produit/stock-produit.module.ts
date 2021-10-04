import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StockProduitComponent } from './list/stock-produit.component';
import { StockProduitDetailComponent } from './detail/stock-produit-detail.component';
import { StockProduitUpdateComponent } from './update/stock-produit-update.component';
import { StockProduitDeleteDialogComponent } from './delete/stock-produit-delete-dialog.component';
import { StockProduitRoutingModule } from './route/stock-produit-routing.module';

@NgModule({
  imports: [SharedModule, StockProduitRoutingModule],
  declarations: [StockProduitComponent, StockProduitDetailComponent, StockProduitUpdateComponent, StockProduitDeleteDialogComponent],
  entryComponents: [StockProduitDeleteDialogComponent],
})
export class StockProduitModule {}
