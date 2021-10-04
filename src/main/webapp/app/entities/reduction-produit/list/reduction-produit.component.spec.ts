import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ReductionProduitService } from '../service/reduction-produit.service';

import { ReductionProduitComponent } from './reduction-produit.component';

describe('Component Tests', () => {
  describe('ReductionProduit Management Component', () => {
    let comp: ReductionProduitComponent;
    let fixture: ComponentFixture<ReductionProduitComponent>;
    let service: ReductionProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReductionProduitComponent],
      })
        .overrideTemplate(ReductionProduitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReductionProduitComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ReductionProduitService);

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
      expect(comp.reductionProduits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
