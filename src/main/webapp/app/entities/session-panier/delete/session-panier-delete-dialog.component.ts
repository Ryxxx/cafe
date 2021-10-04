import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISessionPanier } from '../session-panier.model';
import { SessionPanierService } from '../service/session-panier.service';

@Component({
  templateUrl: './session-panier-delete-dialog.component.html',
})
export class SessionPanierDeleteDialogComponent {
  sessionPanier?: ISessionPanier;

  constructor(protected sessionPanierService: SessionPanierService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sessionPanierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
