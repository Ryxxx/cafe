import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduit, Produit } from '../produit.model';
import { ProduitService } from '../service/produit.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategorieProduit } from 'app/entities/categorie-produit/categorie-produit.model';
import { CategorieProduitService } from 'app/entities/categorie-produit/service/categorie-produit.service';
import { IStockProduit } from 'app/entities/stock-produit/stock-produit.model';
import { StockProduitService } from 'app/entities/stock-produit/service/stock-produit.service';
import { IReductionProduit } from 'app/entities/reduction-produit/reduction-produit.model';
import { ReductionProduitService } from 'app/entities/reduction-produit/service/reduction-produit.service';

@Component({
  selector: 'jhi-produit-update',
  templateUrl: './produit-update.component.html',
})
export class ProduitUpdateComponent implements OnInit {
  isSaving = false;

  categorieProduitsSharedCollection: ICategorieProduit[] = [];
  stockProduitsSharedCollection: IStockProduit[] = [];
  reductionProduitsSharedCollection: IReductionProduit[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    sku: [],
    prix: [],
    reductionId: [],
    dateCreation: [],
    dateModification: [],
    dateSuppression: [],
    categorieProduit: [],
    stockProduit: [],
    reductionProduit: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected produitService: ProduitService,
    protected categorieProduitService: CategorieProduitService,
    protected stockProduitService: StockProduitService,
    protected reductionProduitService: ReductionProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produit }) => {
      this.updateForm(produit);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('cafeApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produit = this.createFromForm();
    if (produit.id !== undefined) {
      this.subscribeToSaveResponse(this.produitService.update(produit));
    } else {
      this.subscribeToSaveResponse(this.produitService.create(produit));
    }
  }

  trackCategorieProduitById(index: number, item: ICategorieProduit): number {
    return item.id!;
  }

  trackStockProduitById(index: number, item: IStockProduit): number {
    return item.id!;
  }

  trackReductionProduitById(index: number, item: IReductionProduit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduit>>): void {
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

  protected updateForm(produit: IProduit): void {
    this.editForm.patchValue({
      id: produit.id,
      nom: produit.nom,
      description: produit.description,
      sku: produit.sku,
      prix: produit.prix,
      reductionId: produit.reductionId,
      dateCreation: produit.dateCreation,
      dateModification: produit.dateModification,
      dateSuppression: produit.dateSuppression,
      categorieProduit: produit.categorieProduit,
      stockProduit: produit.stockProduit,
      reductionProduit: produit.reductionProduit,
    });

    this.categorieProduitsSharedCollection = this.categorieProduitService.addCategorieProduitToCollectionIfMissing(
      this.categorieProduitsSharedCollection,
      produit.categorieProduit
    );
    this.stockProduitsSharedCollection = this.stockProduitService.addStockProduitToCollectionIfMissing(
      this.stockProduitsSharedCollection,
      produit.stockProduit
    );
    this.reductionProduitsSharedCollection = this.reductionProduitService.addReductionProduitToCollectionIfMissing(
      this.reductionProduitsSharedCollection,
      produit.reductionProduit
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieProduitService
      .query()
      .pipe(map((res: HttpResponse<ICategorieProduit[]>) => res.body ?? []))
      .pipe(
        map((categorieProduits: ICategorieProduit[]) =>
          this.categorieProduitService.addCategorieProduitToCollectionIfMissing(
            categorieProduits,
            this.editForm.get('categorieProduit')!.value
          )
        )
      )
      .subscribe((categorieProduits: ICategorieProduit[]) => (this.categorieProduitsSharedCollection = categorieProduits));

    this.stockProduitService
      .query()
      .pipe(map((res: HttpResponse<IStockProduit[]>) => res.body ?? []))
      .pipe(
        map((stockProduits: IStockProduit[]) =>
          this.stockProduitService.addStockProduitToCollectionIfMissing(stockProduits, this.editForm.get('stockProduit')!.value)
        )
      )
      .subscribe((stockProduits: IStockProduit[]) => (this.stockProduitsSharedCollection = stockProduits));

    this.reductionProduitService
      .query()
      .pipe(map((res: HttpResponse<IReductionProduit[]>) => res.body ?? []))
      .pipe(
        map((reductionProduits: IReductionProduit[]) =>
          this.reductionProduitService.addReductionProduitToCollectionIfMissing(
            reductionProduits,
            this.editForm.get('reductionProduit')!.value
          )
        )
      )
      .subscribe((reductionProduits: IReductionProduit[]) => (this.reductionProduitsSharedCollection = reductionProduits));
  }

  protected createFromForm(): IProduit {
    return {
      ...new Produit(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      sku: this.editForm.get(['sku'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      reductionId: this.editForm.get(['reductionId'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      dateSuppression: this.editForm.get(['dateSuppression'])!.value,
      categorieProduit: this.editForm.get(['categorieProduit'])!.value,
      stockProduit: this.editForm.get(['stockProduit'])!.value,
      reductionProduit: this.editForm.get(['reductionProduit'])!.value,
    };
  }
}
