import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetailsCommande } from '../details-commande.model';
import { DetailsCommandeService } from '../service/details-commande.service';

@Component({
  templateUrl: './details-commande-delete-dialog.component.html',
})
export class DetailsCommandeDeleteDialogComponent {
  detailsCommande?: IDetailsCommande;

  constructor(protected detailsCommandeService: DetailsCommandeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detailsCommandeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
