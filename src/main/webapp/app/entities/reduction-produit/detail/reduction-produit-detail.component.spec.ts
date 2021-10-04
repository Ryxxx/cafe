import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReductionProduitDetailComponent } from './reduction-produit-detail.component';

describe('Component Tests', () => {
  describe('ReductionProduit Management Detail Component', () => {
    let comp: ReductionProduitDetailComponent;
    let fixture: ComponentFixture<ReductionProduitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ReductionProduitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ reductionProduit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ReductionProduitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReductionProduitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load reductionProduit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.reductionProduit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
