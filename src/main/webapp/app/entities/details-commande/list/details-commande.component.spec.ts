import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DetailsCommandeService } from '../service/details-commande.service';

import { DetailsCommandeComponent } from './details-commande.component';

describe('Component Tests', () => {
  describe('DetailsCommande Management Component', () => {
    let comp: DetailsCommandeComponent;
    let fixture: ComponentFixture<DetailsCommandeComponent>;
    let service: DetailsCommandeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DetailsCommandeComponent],
      })
        .overrideTemplate(DetailsCommandeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetailsCommandeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DetailsCommandeService);

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
      expect(comp.detailsCommandes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
