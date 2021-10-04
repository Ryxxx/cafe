import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SessionPanierDetailComponent } from './session-panier-detail.component';

describe('Component Tests', () => {
  describe('SessionPanier Management Detail Component', () => {
    let comp: SessionPanierDetailComponent;
    let fixture: ComponentFixture<SessionPanierDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SessionPanierDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ sessionPanier: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SessionPanierDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SessionPanierDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sessionPanier on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sessionPanier).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
