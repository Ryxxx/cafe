import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReductionProduit } from '../reduction-produit.model';
import { ReductionProduitService } from '../service/reduction-produit.service';

@Component({
  templateUrl: './reduction-produit-delete-dialog.component.html',
})
export class ReductionProduitDeleteDialogComponent {
  reductionProduit?: IReductionProduit;

  constructor(protected reductionProduitService: ReductionProduitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reductionProduitService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
