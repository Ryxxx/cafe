import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStockProduit } from '../stock-produit.model';
import { StockProduitService } from '../service/stock-produit.service';
import { StockProduitDeleteDialogComponent } from '../delete/stock-produit-delete-dialog.component';

@Component({
  selector: 'jhi-stock-produit',
  templateUrl: './stock-produit.component.html',
})
export class StockProduitComponent implements OnInit {
  stockProduits?: IStockProduit[];
  isLoading = false;

  constructor(protected stockProduitService: StockProduitService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.stockProduitService.query().subscribe(
      (res: HttpResponse<IStockProduit[]>) => {
        this.isLoading = false;
        this.stockProduits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStockProduit): number {
    return item.id!;
  }

  delete(stockProduit: IStockProduit): void {
    const modalRef = this.modalService.open(StockProduitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.stockProduit = stockProduit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
