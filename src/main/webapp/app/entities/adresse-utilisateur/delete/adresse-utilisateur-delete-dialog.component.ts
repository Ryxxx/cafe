import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdresseUtilisateur } from '../adresse-utilisateur.model';
import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';

@Component({
  templateUrl: './adresse-utilisateur-delete-dialog.component.html',
})
export class AdresseUtilisateurDeleteDialogComponent {
  adresseUtilisateur?: IAdresseUtilisateur;

  constructor(protected adresseUtilisateurService: AdresseUtilisateurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adresseUtilisateurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
