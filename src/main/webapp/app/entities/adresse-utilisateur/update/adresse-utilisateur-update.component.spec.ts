jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AdresseUtilisateurService } from '../service/adresse-utilisateur.service';
import { IAdresseUtilisateur, AdresseUtilisateur } from '../adresse-utilisateur.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { AdresseUtilisateurUpdateComponent } from './adresse-utilisateur-update.component';

describe('Component Tests', () => {
  describe('AdresseUtilisateur Management Update Component', () => {
    let comp: AdresseUtilisateurUpdateComponent;
    let fixture: ComponentFixture<AdresseUtilisateurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let adresseUtilisateurService: AdresseUtilisateurService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdresseUtilisateurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AdresseUtilisateurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdresseUtilisateurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      adresseUtilisateurService = TestBed.inject(AdresseUtilisateurService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const adresseUtilisateur: IAdresseUtilisateur = { id: 456 };
        const userId: IUser = { id: 9247 };
        adresseUtilisateur.userId = userId;

        const userCollection: IUser[] = [{ id: 90504 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [userId];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ adresseUtilisateur });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const adresseUtilisateur: IAdresseUtilisateur = { id: 456 };
        const userId: IUser = { id: 15340 };
        adresseUtilisateur.userId = userId;

        activatedRoute.data = of({ adresseUtilisateur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(adresseUtilisateur));
        expect(comp.usersSharedCollection).toContain(userId);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdresseUtilisateur>>();
        const adresseUtilisateur = { id: 123 };
        jest.spyOn(adresseUtilisateurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adresseUtilisateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adresseUtilisateur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(adresseUtilisateurService.update).toHaveBeenCalledWith(adresseUtilisateur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdresseUtilisateur>>();
        const adresseUtilisateur = new AdresseUtilisateur();
        jest.spyOn(adresseUtilisateurService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adresseUtilisateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: adresseUtilisateur }));
        saveSubject.complete();

        // THEN
        expect(adresseUtilisateurService.create).toHaveBeenCalledWith(adresseUtilisateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdresseUtilisateur>>();
        const adresseUtilisateur = { id: 123 };
        jest.spyOn(adresseUtilisateurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ adresseUtilisateur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(adresseUtilisateurService.update).toHaveBeenCalledWith(adresseUtilisateur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
