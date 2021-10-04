import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;

  produitIdsCollection: IProduit[] = [];

  editForm = this.fb.group({
    id: [],
    detailsCommandeId: [],
    quantite: [],
    dateCreation: [],
    dateModification: [],
    produitId: [],
  });

  constructor(
    protected commandeService: CommandeService,
    protected produitService: ProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      this.updateForm(commande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  trackProduitById(index: number, item: IProduit): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
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

  protected updateForm(commande: ICommande): void {
    this.editForm.patchValue({
      id: commande.id,
      detailsCommandeId: commande.detailsCommandeId,
      quantite: commande.quantite,
      dateCreation: commande.dateCreation,
      dateModification: commande.dateModification,
      produitId: commande.produitId,
    });

    this.produitIdsCollection = this.produitService.addProduitToCollectionIfMissing(this.produitIdsCollection, commande.produitId);
  }

  protected loadRelationshipsOptions(): void {
    this.produitService
      .query({ filter: 'commande-is-null' })
      .pipe(map((res: HttpResponse<IProduit[]>) => res.body ?? []))
      .pipe(
        map((produits: IProduit[]) => this.produitService.addProduitToCollectionIfMissing(produits, this.editForm.get('produitId')!.value))
      )
      .subscribe((produits: IProduit[]) => (this.produitIdsCollection = produits));
  }

  protected createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id'])!.value,
      detailsCommandeId: this.editForm.get(['detailsCommandeId'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      produitId: this.editForm.get(['produitId'])!.value,
    };
  }
}
