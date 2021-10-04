import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IReductionProduit, ReductionProduit } from '../reduction-produit.model';
import { ReductionProduitService } from '../service/reduction-produit.service';

@Component({
  selector: 'jhi-reduction-produit-update',
  templateUrl: './reduction-produit-update.component.html',
})
export class ReductionProduitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    pourcentageReduction: [],
    actif: [],
    dateCreation: [],
    dateModification: [],
    dateSuppression: [],
  });

  constructor(
    protected reductionProduitService: ReductionProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reductionProduit }) => {
      this.updateForm(reductionProduit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reductionProduit = this.createFromForm();
    if (reductionProduit.id !== undefined) {
      this.subscribeToSaveResponse(this.reductionProduitService.update(reductionProduit));
    } else {
      this.subscribeToSaveResponse(this.reductionProduitService.create(reductionProduit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReductionProduit>>): void {
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

  protected updateForm(reductionProduit: IReductionProduit): void {
    this.editForm.patchValue({
      id: reductionProduit.id,
      nom: reductionProduit.nom,
      description: reductionProduit.description,
      pourcentageReduction: reductionProduit.pourcentageReduction,
      actif: reductionProduit.actif,
      dateCreation: reductionProduit.dateCreation,
      dateModification: reductionProduit.dateModification,
      dateSuppression: reductionProduit.dateSuppression,
    });
  }

  protected createFromForm(): IReductionProduit {
    return {
      ...new ReductionProduit(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      pourcentageReduction: this.editForm.get(['pourcentageReduction'])!.value,
      actif: this.editForm.get(['actif'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      dateSuppression: this.editForm.get(['dateSuppression'])!.value,
    };
  }
}
