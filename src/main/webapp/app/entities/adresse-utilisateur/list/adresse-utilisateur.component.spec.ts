import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';

import { AdresseUtilisateurComponent } from './adresse-utilisateur.component';

describe('Component Tests', () => {
  describe('AdresseUtilisateur Management Component', () => {
    let comp: AdresseUtilisateurComponent;
    let fixture: ComponentFixture<AdresseUtilisateurComponent>;
    let service: AdresseUtilisateurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdresseUtilisateurComponent],
      })
        .overrideTemplate(AdresseUtilisateurComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdresseUtilisateurComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AdresseUtilisateurService);

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
      expect(comp.adresseUtilisateurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
