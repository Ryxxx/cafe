import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISessionPanier, SessionPanier } from '../session-panier.model';
import { SessionPanierService } from '../service/session-panier.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPanier } from 'app/entities/panier/panier.model';
import { PanierService } from 'app/entities/panier/service/panier.service';

@Component({
  selector: 'jhi-session-panier-update',
  templateUrl: './session-panier-update.component.html',
})
export class SessionPanierUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  paniersSharedCollection: IPanier[] = [];

  editForm = this.fb.group({
    id: [],
    total: [],
    dateCreation: [],
    dateModification: [],
    userId: [],
    panier: [],
  });

  constructor(
    protected sessionPanierService: SessionPanierService,
    protected userService: UserService,
    protected panierService: PanierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionPanier }) => {
      this.updateForm(sessionPanier);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sessionPanier = this.createFromForm();
    if (sessionPanier.id !== undefined) {
      this.subscribeToSaveResponse(this.sessionPanierService.update(sessionPanier));
    } else {
      this.subscribeToSaveResponse(this.sessionPanierService.create(sessionPanier));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPanierById(index: number, item: IPanier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISessionPanier>>): void {
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

  protected updateForm(sessionPanier: ISessionPanier): void {
    this.editForm.patchValue({
      id: sessionPanier.id,
      total: sessionPanier.total,
      dateCreation: sessionPanier.dateCreation,
      dateModification: sessionPanier.dateModification,
      userId: sessionPanier.userId,
      panier: sessionPanier.panier,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, sessionPanier.userId);
    this.paniersSharedCollection = this.panierService.addPanierToCollectionIfMissing(this.paniersSharedCollection, sessionPanier.panier);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('userId')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.panierService
      .query()
      .pipe(map((res: HttpResponse<IPanier[]>) => res.body ?? []))
      .pipe(map((paniers: IPanier[]) => this.panierService.addPanierToCollectionIfMissing(paniers, this.editForm.get('panier')!.value)))
      .subscribe((paniers: IPanier[]) => (this.paniersSharedCollection = paniers));
  }

  protected createFromForm(): ISessionPanier {
    return {
      ...new SessionPanier(),
      id: this.editForm.get(['id'])!.value,
      total: this.editForm.get(['total'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      panier: this.editForm.get(['panier'])!.value,
    };
  }
}
