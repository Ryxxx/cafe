import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdresseUtilisateur } from '../adresse-utilisateur.model';
import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';
import { AdresseUtilisateurDeleteDialogComponent } from '../delete/adresse-utilisateur-delete-dialog.component';

@Component({
  selector: 'jhi-adresse-utilisateur',
  templateUrl: './adresse-utilisateur.component.html',
})
export class AdresseUtilisateurComponent implements OnInit {
  adresseUtilisateurs?: IAdresseUtilisateur[];
  isLoading = false;

  constructor(protected adresseUtilisateurService: AdresseUtilisateurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.adresseUtilisateurService.query().subscribe(
      (res: HttpResponse<IAdresseUtilisateur[]>) => {
        this.isLoading = false;
        this.adresseUtilisateurs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAdresseUtilisateur): number {
    return item.id!;
  }

  delete(adresseUtilisateur: IAdresseUtilisateur): void {
    const modalRef = this.modalService.open(AdresseUtilisateurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.adresseUtilisateur = adresseUtilisateur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
