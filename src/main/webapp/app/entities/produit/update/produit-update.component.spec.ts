jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProduitService } from '../service/produit.service';
import { IProduit, Produit } from '../produit.model';
import { ICategorieProduit } from 'app/entities/categorie-produit/categorie-produit.model';
import { CategorieProduitService } from 'app/entities/categorie-produit/service/categorie-produit.service';
import { IStockProduit } from 'app/entities/stock-produit/stock-produit.model';
import { StockProduitService } from 'app/entities/stock-produit/service/stock-produit.service';
import { IReductionProduit } from 'app/entities/reduction-produit/reduction-produit.model';
import { ReductionProduitService } from 'app/entities/reduction-produit/service/reduction-produit.service';

import { ProduitUpdateComponent } from './produit-update.component';

describe('Component Tests', () => {
  describe('Produit Management Update Component', () => {
    let comp: ProduitUpdateComponent;
    let fixture: ComponentFixture<ProduitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let produitService: ProduitService;
    let categorieProduitService: CategorieProduitService;
    let stockProduitService: StockProduitService;
    let reductionProduitService: ReductionProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProduitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProduitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProduitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      produitService = TestBed.inject(ProduitService);
      categorieProduitService = TestBed.inject(CategorieProduitService);
      stockProduitService = TestBed.inject(StockProduitService);
      reductionProduitService = TestBed.inject(ReductionProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CategorieProduit query and add missing value', () => {
        const produit: IProduit = { id: 456 };
        const categorieProduit: ICategorieProduit = { id: 54812 };
        produit.categorieProduit = categorieProduit;

        const categorieProduitCollection: ICategorieProduit[] = [{ id: 38309 }];
        jest.spyOn(categorieProduitService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieProduitCollection })));
        const additionalCategorieProduits = [categorieProduit];
        const expectedCollection: ICategorieProduit[] = [...additionalCategorieProduits, ...categorieProduitCollection];
        jest.spyOn(categorieProduitService, 'addCategorieProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        expect(categorieProduitService.query).toHaveBeenCalled();
        expect(categorieProduitService.addCategorieProduitToCollectionIfMissing).toHaveBeenCalledWith(
          categorieProduitCollection,
          ...additionalCategorieProduits
        );
        expect(comp.categorieProduitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call StockProduit query and add missing value', () => {
        const produit: IProduit = { id: 456 };
        const stockProduit: IStockProduit = { id: 46121 };
        produit.stockProduit = stockProduit;

        const stockProduitCollection: IStockProduit[] = [{ id: 33426 }];
        jest.spyOn(stockProduitService, 'query').mockReturnValue(of(new HttpResponse({ body: stockProduitCollection })));
        const additionalStockProduits = [stockProduit];
        const expectedCollection: IStockProduit[] = [...additionalStockProduits, ...stockProduitCollection];
        jest.spyOn(stockProduitService, 'addStockProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        expect(stockProduitService.query).toHaveBeenCalled();
        expect(stockProduitService.addStockProduitToCollectionIfMissing).toHaveBeenCalledWith(
          stockProduitCollection,
          ...additionalStockProduits
        );
        expect(comp.stockProduitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ReductionProduit query and add missing value', () => {
        const produit: IProduit = { id: 456 };
        const reductionProduit: IReductionProduit = { id: 69283 };
        produit.reductionProduit = reductionProduit;

        const reductionProduitCollection: IReductionProduit[] = [{ id: 40902 }];
        jest.spyOn(reductionProduitService, 'query').mockReturnValue(of(new HttpResponse({ body: reductionProduitCollection })));
        const additionalReductionProduits = [reductionProduit];
        const expectedCollection: IReductionProduit[] = [...additionalReductionProduits, ...reductionProduitCollection];
        jest.spyOn(reductionProduitService, 'addReductionProduitToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        expect(reductionProduitService.query).toHaveBeenCalled();
        expect(reductionProduitService.addReductionProduitToCollectionIfMissing).toHaveBeenCalledWith(
          reductionProduitCollection,
          ...additionalReductionProduits
        );
        expect(comp.reductionProduitsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const produit: IProduit = { id: 456 };
        const categorieProduit: ICategorieProduit = { id: 9032 };
        produit.categorieProduit = categorieProduit;
        const stockProduit: IStockProduit = { id: 69184 };
        produit.stockProduit = stockProduit;
        const reductionProduit: IReductionProduit = { id: 8675 };
        produit.reductionProduit = reductionProduit;

        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(produit));
        expect(comp.categorieProduitsSharedCollection).toContain(categorieProduit);
        expect(comp.stockProduitsSharedCollection).toContain(stockProduit);
        expect(comp.reductionProduitsSharedCollection).toContain(reductionProduit);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Produit>>();
        const produit = { id: 123 };
        jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: produit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(produitService.update).toHaveBeenCalledWith(produit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Produit>>();
        const produit = new Produit();
        jest.spyOn(produitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: produit }));
        saveSubject.complete();

        // THEN
        expect(produitService.create).toHaveBeenCalledWith(produit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Produit>>();
        const produit = { id: 123 };
        jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ produit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(produitService.update).toHaveBeenCalledWith(produit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCategorieProduitById', () => {
        it('Should return tracked CategorieProduit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategorieProduitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackStockProduitById', () => {
        it('Should return tracked StockProduit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStockProduitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackReductionProduitById', () => {
        it('Should return tracked ReductionProduit primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackReductionProduitById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
