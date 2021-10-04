import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StockProduitDetailComponent } from './stock-produit-detail.component';

describe('Component Tests', () => {
  describe('StockProduit Management Detail Component', () => {
    let comp: StockProduitDetailComponent;
    let fixture: ComponentFixture<StockProduitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StockProduitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ stockProduit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StockProduitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StockProduitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load stockProduit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.stockProduit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
