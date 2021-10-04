import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IStockProduit, StockProduit } from '../stock-produit.model';
import { StockProduitService } from '../service/stock-produit.service';

@Component({
  selector: 'jhi-stock-produit-update',
  templateUrl: './stock-produit-update.component.html',
})
export class StockProduitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    quantite: [],
    dateCreation: [],
    dateModification: [],
    dateSuppression: [],
  });

  constructor(protected stockProduitService: StockProduitService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stockProduit }) => {
      this.updateForm(stockProduit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stockProduit = this.createFromForm();
    if (stockProduit.id !== undefined) {
      this.subscribeToSaveResponse(this.stockProduitService.update(stockProduit));
    } else {
      this.subscribeToSaveResponse(this.stockProduitService.create(stockProduit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStockProduit>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(stockProduit: IStockProduit): void {
    this.editForm.patchValue({
      id: stockProduit.id,
      quantite: stockProduit.quantite,
      dateCreation: stockProduit.dateCreation,
      dateModification: stockProduit.dateModification,
      dateSuppression: stockProduit.dateSuppression,
    });
  }

  protected createFromForm(): IStockProduit {
    return {
      ...new StockProduit(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      dateSuppression: this.editForm.get(['dateSuppression'])!.value,
    };
  }
}
