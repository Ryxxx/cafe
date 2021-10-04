jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReductionProduitService } from '../service/reduction-produit.service';
import { IReductionProduit, ReductionProduit } from '../reduction-produit.model';

import { ReductionProduitUpdateComponent } from './reduction-produit-update.component';

describe('Component Tests', () => {
  describe('ReductionProduit Management Update Component', () => {
    let comp: ReductionProduitUpdateComponent;
    let fixture: ComponentFixture<ReductionProduitUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reductionProduitService: ReductionProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReductionProduitUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReductionProduitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReductionProduitUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reductionProduitService = TestBed.inject(ReductionProduitService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const reductionProduit: IReductionProduit = { id: 456 };

        activatedRoute.data = of({ reductionProduit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(reductionProduit));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReductionProduit>>();
        const reductionProduit = { id: 123 };
        jest.spyOn(reductionProduitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reductionProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reductionProduit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reductionProduitService.update).toHaveBeenCalledWith(reductionProduit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReductionProduit>>();
        const reductionProduit = new ReductionProduit();
        jest.spyOn(reductionProduitService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reductionProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reductionProduit }));
        saveSubject.complete();

        // THEN
        expect(reductionProduitService.create).toHaveBeenCalledWith(reductionProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ReductionProduit>>();
        const reductionProduit = { id: 123 };
        jest.spyOn(reductionProduitService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ reductionProduit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reductionProduitService.update).toHaveBeenCalledWith(reductionProduit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
