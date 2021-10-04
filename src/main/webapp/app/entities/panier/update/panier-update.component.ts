import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPanier, Panier } from '../panier.model';
import { PanierService } from '../service/panier.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

@Component({
  selector: 'jhi-panier-update',
  templateUrl: './panier-update.component.html',
})
export class PanierUpdateComponent implements OnInit {
  isSaving = false;

  produitIdsCollection: IProduit[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    dateCreation: [],
    dateModification: [],
    produitId: [],
  });

  constructor(
    protected panierService: PanierService,
    protected produitService: ProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ panier }) => {
      this.updateForm(panier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const panier = this.createFromForm();
    if (panier.id !== undefined) {
      this.subscribeToSaveResponse(this.panierService.update(panier));
    } else {
      this.subscribeToSaveResponse(this.panierService.create(panier));
    }
  }

  trackProduitById(index: number, item: IProduit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPanier>>): void {
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

  protected updateForm(panier: IPanier): void {
    this.editForm.patchValue({
      id: panier.id,
      quantite: panier.quantite,
      dateCreation: panier.dateCreation,
      dateModification: panier.dateModification,
      produitId: panier.produitId,
    });

    this.produitIdsCollection = this.produitService.addProduitToCollectionIfMissing(this.produitIdsCollection, panier.produitId);
  }

  protected loadRelationshipsOptions(): void {
    this.produitService
      .query({ filter: 'panier-is-null' })
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) => this.produitService.addProduitToCollectionIfMissing(produits, this.editForm.get('produitId')!.value))
      )
      .subscribe((produits: IProduit[]) => (this.produitIdsCollection = produits));
  }

  protected createFromForm(): IPanier {
    return {
      ...new Panier(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      produitId: this.editForm.get(['produitId'])!.value,
    };
  }
}
