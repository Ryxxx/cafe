import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDetailsCommande, DetailsCommande } from '../details-commande.model';
import { DetailsCommandeService } from '../service/details-commande.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPaiement } from 'app/entities/paiement/paiement.model';
import { PaiementService } from 'app/entities/paiement/service/paiement.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

@Component({
  selector: 'jhi-details-commande-update',
  templateUrl: './details-commande-update.component.html',
})
export class DetailsCommandeUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  paimentIdsCollection: IPaiement[] = [];
  commandesSharedCollection: ICommande[] = [];

  editForm = this.fb.group({
    id: [],
    total: [],
    dateCreation: [],
    dateModification: [],
    userId: [],
    paimentId: [],
    commande: [],
  });

  constructor(
    protected detailsCommandeService: DetailsCommandeService,
    protected userService: UserService,
    protected paiementService: PaiementService,
    protected commandeService: CommandeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailsCommande }) => {
      this.updateForm(detailsCommande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detailsCommande = this.createFromForm();
    if (detailsCommande.id !== undefined) {
      this.subscribeToSaveResponse(this.detailsCommandeService.update(detailsCommande));
    } else {
      this.subscribeToSaveResponse(this.detailsCommandeService.create(detailsCommande));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPaiementById(index: number, item: IPaiement): number {
    return item.id!;
  }

  trackCommandeById(index: number, item: ICommande): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetailsCommande>>): void {
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

  protected updateForm(detailsCommande: IDetailsCommande): void {
    this.editForm.patchValue({
      id: detailsCommande.id,
      total: detailsCommande.total,
      dateCreation: detailsCommande.dateCreation,
      dateModification: detailsCommande.dateModification,
      userId: detailsCommande.userId,
      paimentId: detailsCommande.paimentId,
      commande: detailsCommande.commande,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, detailsCommande.userId);
    this.paimentIdsCollection = this.paiementService.addPaiementToCollectionIfMissing(this.paimentIdsCollection, detailsCommande.paimentId);
    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(
      this.commandesSharedCollection,
      detailsCommande.commande
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('userId')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.paiementService
      .query({ filter: 'detailscommande-is-null' })
      .pipe(map((res: HttpResponse<IPaiement[]>) => res.body ?? []))
      .pipe(
        map((paiements: IPaiement[]) =>
          this.paiementService.addPaiementToCollectionIfMissing(paiements, this.editForm.get('paimentId')!.value)
        )
      )
      .subscribe((paiements: IPaiement[]) => (this.paimentIdsCollection = paiements));

    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing(commandes, this.editForm.get('commande')!.value)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));
  }

  protected createFromForm(): IDetailsCommande {
    return {
      ...new DetailsCommande(),
      id: this.editForm.get(['id'])!.value,
      total: this.editForm.get(['total'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value,
      dateModification: this.editForm.get(['dateModification'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      paimentId: this.editForm.get(['paimentId'])!.value,
      commande: this.editForm.get(['commande'])!.value,
    };
  }
}
