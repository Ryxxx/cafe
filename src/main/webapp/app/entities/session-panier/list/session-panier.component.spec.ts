import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SessionPanierService } from '../service/session-panier.service';

import { SessionPanierComponent } from './session-panier.component';

describe('Component Tests', () => {
  describe('SessionPanier Management Component', () => {
    let comp: SessionPanierComponent;
    let fixture: ComponentFixture<SessionPanierComponent>;
    let service: SessionPanierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SessionPanierComponent],
      })
        .overrideTemplate(SessionPanierComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SessionPanierComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SessionPanierService);

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
      expect(comp.sessionPaniers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
