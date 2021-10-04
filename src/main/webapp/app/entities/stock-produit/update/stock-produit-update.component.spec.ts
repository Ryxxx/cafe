jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StockProduitService } from '../service/stock-produit.service';
import { IStockProduit, StockProduit } from '../stock-produit.model';

import { StockProduitUpdateComponent } from './stock-produit-update.component';

describe('Component Tests', () => {
  describe('StockProduit Management Update Component', () => {
    let comp: StockProduitUpdateComponent;
    let fixture: ComponentFixture<StockProduitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let stockProduitService: StockProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StockProduitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StockProduitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StockProduitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      stockProduitService = TestBed.inject(StockProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const stockProduit: IStockProduit = { id: 456 };

        activatedRoute.data = of({ stockProduit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(stockProduit));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StockProduit>>();
        const stockProduit = { id: 123 };
        jest.spyOn(stockProduitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ stockProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: stockProduit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(stockProduitService.update).toHaveBeenCalledWith(stockProduit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StockProduit>>();
        const stockProduit = new StockProduit();
        jest.spyOn(stockProduitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ stockProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: stockProduit }));
        saveSubject.complete();

        // THEN
        expect(stockProduitService.create).toHaveBeenCalledWith(stockProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<StockProduit>>();
        const stockProduit = { id: 123 };
        jest.spyOn(stockProduitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ stockProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(stockProduitService.update).toHaveBeenCalledWith(stockProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
