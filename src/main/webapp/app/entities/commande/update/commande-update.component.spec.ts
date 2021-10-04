jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommandeService } from '../service/commande.service';
import { ICommande, Commande } from '../commande.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

import { CommandeUpdateComponent } from './commande-update.component';

describe('Component Tests', () => {
  describe('Commande Management Update Component', () => {
    let comp: CommandeUpdateComponent;
    let fixture: ComponentFixture<CommandeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commandeService: CommandeService;
    let produitService: ProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommandeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommandeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommandeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commandeService = TestBed.inject(CommandeService);
      produitService = TestBed.inject(ProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call produitId query and add missing value', () => {
        const commande: ICommande = { id: 456 };
        const produitId: IProduit = { id: 97161 };
        commande.produitId = produitId;

        const produitIdCollection: IProduit[] = [{ id: 28744 }];
        jest.spyOn(produitService, 'query').mockReturnValue(of(new HttpResponse({ body: produitIdCollection })));
        const expectedCollection: IProduit[] = [produitId, ...produitIdCollection];
        jest.spyOn(produitService, 'addProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitIdCollection, produitId);
        expect(comp.produitIdsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const commande: ICommande = { id: 456 };
        const produitId: IProduit = { id: 94032 };
        commande.produitId = produitId;

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(commande));
        expect(comp.produitIdsCollection).toContain(produitId);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Commande>>();
        const commande = { id: 123 };
        jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commande }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commandeService.update).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Commande>>();
        const commande = new Commande();
        jest.spyOn(commandeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commande }));
        saveSubject.complete();

        // THEN
        expect(commandeService.create).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Commande>>();
        const commande = { id: 123 };
        jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commandeService.update).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProduitById', () => {
        it('Should return tracked Produit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProduitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
