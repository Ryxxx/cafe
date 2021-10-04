jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PanierService } from '../service/panier.service';
import { IPanier, Panier } from '../panier.model';
import { IProduit } from 'app/entities/produit/produit.model';
import { ProduitService } from 'app/entities/produit/service/produit.service';

import { PanierUpdateComponent } from './panier-update.component';

describe('Component Tests', () => {
  describe('Panier Management Update Component', () => {
    let comp: PanierUpdateComponent;
    let fixture: ComponentFixture<PanierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let panierService: PanierService;
    let produitService: ProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PanierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PanierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PanierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      panierService = TestBed.inject(PanierService);
      produitService = TestBed.inject(ProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call produitId query and add missing value', () => {
        const panier: IPanier = { id: 456 };
        const produitId: IProduit = { id: 20161 };
        panier.produitId = produitId;

        const produitIdCollection: IProduit[] = [{ id: 11736 }];
        jest.spyOn(produitService, 'query').mockReturnValue(of(new HttpResponse({ body: produitIdCollection })));
        const expectedCollection: IProduit[] = [produitId, ...produitIdCollection];
        jest.spyOn(produitService, 'addProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ panier });
        comp.ngOnInit();

        expect(produitService.query).toHaveBeenCalled();
        expect(produitService.addProduitToCollectionIfMissing).toHaveBeenCalledWith(produitIdCollection, produitId);
        expect(comp.produitIdsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const panier: IPanier = { id: 456 };
        const produitId: IProduit = { id: 96873 };
        panier.produitId = produitId;

        activatedRoute.data = of({ panier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(panier));
        expect(comp.produitIdsCollection).toContain(produitId);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Panier>>();
        const panier = { id: 123 };
        jest.spyOn(panierService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ panier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: panier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(panierService.update).toHaveBeenCalledWith(panier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Panier>>();
        const panier = new Panier();
        jest.spyOn(panierService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ panier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: panier }));
        saveSubject.complete();

        // THEN
        expect(panierService.create).toHaveBeenCalledWith(panier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Panier>>();
        const panier = { id: 123 };
        jest.spyOn(panierService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ panier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(panierService.update).toHaveBeenCalledWith(panier);
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
