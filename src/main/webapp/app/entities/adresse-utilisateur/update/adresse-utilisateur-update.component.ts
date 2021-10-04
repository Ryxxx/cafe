import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAdresseUtilisateur, AdresseUtilisateur } from '../adresse-utilisateur.model';
import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-adresse-utilisateur-update',
  templateUrl: './adresse-utilisateur-update.component.html',
})
export class AdresseUtilisateurUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    adresseLigne1: [],
    adresseLigne2: [],
    ville: [],
    codePostal: [],
    pays: [],
    telephone: [],
    userId: [],
  });

  constructor(
    protected adresseUtilisateurService: AdresseUtilisateurService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adresseUtilisateur }) => {
      this.updateForm(adresseUtilisateur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adresseUtilisateur = this.createFromForm();
    if (adresseUtilisateur.id !== undefined) {
      this.subscribeToSaveResponse(this.adresseUtilisateurService.update(adresseUtilisateur));
    } else {
      this.subscribeToSaveResponse(this.adresseUtilisateurService.create(adresseUtilisateur));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdresseUtilisateur>>): void {
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

  protected updateForm(adresseUtilisateur: IAdresseUtilisateur): void {
    this.editForm.patchValue({
      id: adresseUtilisateur.id,
      adresseLigne1: adresseUtilisateur.adresseLigne1,
      adresseLigne2: adresseUtilisateur.adresseLigne2,
      ville: adresseUtilisateur.ville,
      codePostal: adresseUtilisateur.codePostal,
      pays: adresseUtilisateur.pays,
      telephone: adresseUtilisateur.telephone,
      userId: adresseUtilisateur.userId,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, adresseUtilisateur.userId);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('userId')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IAdresseUtilisateur {
    return {
      ...new AdresseUtilisateur(),
      id: this.editForm.get(['id'])!.value,
      adresseLigne1: this.editForm.get(['adresseLigne1'])!.value,
      adresseLigne2: this.editForm.get(['adresseLigne2'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      pays: this.editForm.get(['pays'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }
}
