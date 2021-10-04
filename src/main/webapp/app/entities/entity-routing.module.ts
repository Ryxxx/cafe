import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'adresse-utilisateur',
        data: { pageTitle: 'cafeApp.adresseUtilisateur.home.title' },
        loadChildren: () => import('./adresse-utilisateur/adresse-utilisateur.module').then(m => m.AdresseUtilisateurModule),
      },
      {
        path: 'produit',
        data: { pageTitle: 'cafeApp.produit.home.title' },
        loadChildren: () => import('./produit/produit.module').then(m => m.ProduitModule),
      },
      {
        path: 'categorie-produit',
        data: { pageTitle: 'cafeApp.categorieProduit.home.title' },
        loadChildren: () => import('./categorie-produit/categorie-produit.module').then(m => m.CategorieProduitModule),
      },
      {
        path: 'stock-produit',
        data: { pageTitle: 'cafeApp.stockProduit.home.title' },
        loadChildren: () => import('./stock-produit/stock-produit.module').then(m => m.StockProduitModule),
      },
      {
        path: 'reduction-produit',
        data: { pageTitle: 'cafeApp.reductionProduit.home.title' },
        loadChildren: () => import('./reduction-produit/reduction-produit.module').then(m => m.ReductionProduitModule),
      },
      {
        path: 'panier',
        data: { pageTitle: 'cafeApp.panier.home.title' },
        loadChildren: () => import('./panier/panier.module').then(m => m.PanierModule),
      },
      {
        path: 'session-panier',
        data: { pageTitle: 'cafeApp.sessionPanier.home.title' },
        loadChildren: () => import('./session-panier/session-panier.module').then(m => m.SessionPanierModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'cafeApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'details-commande',
        data: { pageTitle: 'cafeApp.detailsCommande.home.title' },
        loadChildren: () => import('./details-commande/details-commande.module').then(m => m.DetailsCommandeModule),
      },
      {
        path: 'paiement',
        data: { pageTitle: 'cafeApp.paiement.home.title' },
        loadChildren: () => import('./paiement/paiement.module').then(m => m.PaiementModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
