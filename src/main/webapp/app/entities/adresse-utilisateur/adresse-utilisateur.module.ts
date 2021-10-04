import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseUtilisateurComponent } from './list/adresse-utilisateur.component';
import { AdresseUtilisateurDetailComponent } from './detail/adresse-utilisateur-detail.component';
import { AdresseUtilisateurUpdateComponent } from './update/adresse-utilisateur-update.component';
import { AdresseUtilisateurDeleteDialogComponent } from './delete/adresse-utilisateur-delete-dialog.component';
import { AdresseUtilisateurRoutingModule } from './route/adresse-utilisateur-routing.module';

@NgModule({
  imports: [SharedModule, AdresseUtilisateurRoutingModule],
  declarations: [
    AdresseUtilisateurComponent,
    AdresseUtilisateurDetailComponent,
    AdresseUtilisateurUpdateComponent,
    AdresseUtilisateurDeleteDialogComponent,
  ],
  entryComponents: [AdresseUtilisateurDeleteDialogComponent],
})
export class AdresseUtilisateurModule {}
