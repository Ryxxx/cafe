import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICategorieProduit, CategorieProduit } from '../categorie-produit.model';
import { CategorieProduitService } from '../service/categorie-produit.service';

@Component({
  selector: 'jhi-categorie-produit-update',
  templateUrl: './categorie-produit-update.component.html',
})
export class CategorieProduitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    dateCreation: [],
    dateModification: [],
    dateSuppression: [],
  });

  constructor(
    protected categorieProduitService: CategorieProduitService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorieProduit }) => {
      this.updateForm(categorieProduit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categorieProduit = this.createFromForm();
    if (categorieProduit.id !== undefined) {
      this.subscribeToSaveResponse(this.categorieProduitService.update(categorieProduit));
    } else {
      this.subscribeToSaveResponse(this.categorieProduitService.create(categorieProduit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategorieProduit>>): void {
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

  protected updateForm(categorieProduit: ICategorieProduit): void {
    this.editForm.patchValue({
      id: categorieProduit.id,
      nom: categorieProduit.nom,
      description: categorieProduit.description,
      dateCreation: categorieProduit.dateCreation,
      dateModification: categorieProduit.dateModification,
      dateSuppression: categorieProduit.dateSuppression,
    });
  }

  protected createFromForm(): ICategorieProduit {
    return {
      ...new CategorieProduit(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      dateSuppression: this.editForm.get(['dateSuppression'])!.value,
    };
  }
}
