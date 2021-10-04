import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStockProduit } from '../stock-produit.model';
import { StockProduitService } from '../service/stock-produit.service';

@Component({
  templateUrl: './stock-produit-delete-dialog.component.html',
})
export class StockProduitDeleteDialogComponent {
  stockProduit?: IStockProduit;

  constructor(protected stockProduitService: StockProduitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stockProduitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
