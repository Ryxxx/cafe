import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DetailsCommandeDetailComponent } from './details-commande-detail.component';

describe('Component Tests', () => {
  describe('DetailsCommande Management Detail Component', () => {
    let comp: DetailsCommandeDetailComponent;
    let fixture: ComponentFixture<DetailsCommandeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DetailsCommandeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ detailsCommande: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DetailsCommandeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DetailsCommandeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load detailsCommande on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.detailsCommande).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
