import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CategorieProduitService } from '../service/categorie-produit.service';

import { CategorieProduitComponent } from './categorie-produit.component';

describe('Component Tests', () => {
  describe('CategorieProduit Management Component', () => {
    let comp: CategorieProduitComponent;
    let fixture: ComponentFixture<CategorieProduitComponent>;
    let service: CategorieProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CategorieProduitComponent],
      })
        .overrideTemplate(CategorieProduitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CategorieProduitComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CategorieProduitService);

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
      expect(comp.categorieProduits?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
