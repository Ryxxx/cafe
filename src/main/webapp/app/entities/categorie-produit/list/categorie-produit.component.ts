import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategorieProduit } from '../categorie-produit.model';
import { CategorieProduitService } from '../service/categorie-produit.service';
import { CategorieProduitDeleteDialogComponent } from '../delete/categorie-produit-delete-dialog.component';

@Component({
  selector: 'jhi-categorie-produit',
  templateUrl: './categorie-produit.component.html',
})
export class CategorieProduitComponent implements OnInit {
  categorieProduits?: ICategorieProduit[];
  isLoading = false;

  constructor(protected categorieProduitService: CategorieProduitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.categorieProduitService.query().subscribe(
      (res: HttpResponse<ICategorieProduit[]>) => {
        this.isLoading = false;
        this.categorieProduits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICategorieProduit): number {
    return item.id!;
  }

  delete(categorieProduit: ICategorieProduit): void {
    const modalRef = this.modalService.open(CategorieProduitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categorieProduit = categorieProduit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
