import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetailsCommande } from '../details-commande.model';
import { DetailsCommandeService } from '../service/details-commande.service';
import { DetailsCommandeDeleteDialogComponent } from '../delete/details-commande-delete-dialog.component';

@Component({
  selector: 'jhi-details-commande',
  templateUrl: './details-commande.component.html',
})
export class DetailsCommandeComponent implements OnInit {
  detailsCommandes?: IDetailsCommande[];
  isLoading = false;

  constructor(protected detailsCommandeService: DetailsCommandeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.detailsCommandeService.query().subscribe(
      (res: HttpResponse<IDetailsCommande[]>) => {
        this.isLoading = false;
        this.detailsCommandes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDetailsCommande): number {
    return item.id!;
  }

  delete(detailsCommande: IDetailsCommande): void {
    const modalRef = this.modalService.open(DetailsCommandeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.detailsCommande = detailsCommande;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
