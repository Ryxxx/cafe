import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdresseUtilisateurDetailComponent } from './adresse-utilisateur-detail.component';

describe('Component Tests', () => {
  describe('AdresseUtilisateur Management Detail Component', () => {
    let comp: AdresseUtilisateurDetailComponent;
    let fixture: ComponentFixture<AdresseUtilisateurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AdresseUtilisateurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ adresseUtilisateur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AdresseUtilisateurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdresseUtilisateurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load adresseUtilisateur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.adresseUtilisateur).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
