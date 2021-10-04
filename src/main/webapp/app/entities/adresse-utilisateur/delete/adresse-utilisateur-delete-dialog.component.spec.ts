jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';

import { AdresseUtilisateurDeleteDialogComponent } from './adresse-utilisateur-delete-dialog.component';

describe('Component Tests', () => {
  describe('AdresseUtilisateur Management Delete Component', () => {
    let comp: AdresseUtilisateurDeleteDialogComponent;
    let fixture: ComponentFixture<AdresseUtilisateurDeleteDialogComponent>;
    let service: AdresseUtilisateurService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdresseUtilisateurDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AdresseUtilisateurDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdresseUtilisateurDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AdresseUtilisateurService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
