import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReductionProduit } from '../reduction-produit.model';
import { ReductionProduitService } from '../service/reduction-produit.service';
import { ReductionProduitDeleteDialogComponent } from '../delete/reduction-produit-delete-dialog.component';

@Component({
  selector: 'jhi-reduction-produit',
  templateUrl: './reduction-produit.component.html',
})
export class ReductionProduitComponent implements OnInit {
  reductionProduits?: IReductionProduit[];
  isLoading = false;

  constructor(protected reductionProduitService: ReductionProduitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.reductionProduitService.query().subscribe(
      (res: HttpResponse<IReductionProduit[]>) => {
        this.isLoading = false;
        this.reductionProduits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IReductionProduit): number {
    return item.id!;
  }

  delete(reductionProduit: IReductionProduit): void {
    const modalRef = this.modalService.open(ReductionProduitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reductionProduit = reductionProduit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
