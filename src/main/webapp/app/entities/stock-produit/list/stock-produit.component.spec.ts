import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StockProduitService } from '../service/stock-produit.service';

import { StockProduitComponent } from './stock-produit.component';

describe('Component Tests', () => {
  describe('StockProduit Management Component', () => {
    let comp: StockProduitComponent;
    let fixture: ComponentFixture<StockProduitComponent>;
    let service: StockProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StockProduitComponent],
      })
        .overrideTemplate(StockProduitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StockProduitComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StockProduitService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.stockProduits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
