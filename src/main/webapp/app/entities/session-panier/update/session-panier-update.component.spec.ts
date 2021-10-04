jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SessionPanierService } from '../service/session-panier.service';
import { ISessionPanier, SessionPanier } from '../session-panier.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPanier } from 'app/entities/panier/panier.model';
import { PanierService } from 'app/entities/panier/service/panier.service';

import { SessionPanierUpdateComponent } from './session-panier-update.component';

describe('Component Tests', () => {
  describe('SessionPanier Management Update Component', () => {
    let comp: SessionPanierUpdateComponent;
    let fixture: ComponentFixture<SessionPanierUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sessionPanierService: SessionPanierService;
    let userService: UserService;
    let panierService: PanierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SessionPanierUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SessionPanierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SessionPanierUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sessionPanierService = TestBed.inject(SessionPanierService);
      userService = TestBed.inject(UserService);
      panierService = TestBed.inject(PanierService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const sessionPanier: ISessionPanier = { id: 456 };
        const userId: IUser = { id: 14786 };
        sessionPanier.userId = userId;

        const userCollection: IUser[] = [{ id: 91253 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [userId];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ sessionPanier });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Panier query and add missing value', () => {
        const sessionPanier: ISessionPanier = { id: 456 };
        const panier: IPanier = { id: 16765 };
        sessionPanier.panier = panier;

        const panierCollection: IPanier[] = [{ id: 84004 }];
        jest.spyOn(panierService, 'query').mockReturnValue(of(new HttpResponse({ body: panierCollection })));
        const additionalPaniers = [panier];
        const expectedCollection: IPanier[] = [...additionalPaniers, ...panierCollection];
        jest.spyOn(panierService, 'addPanierToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ sessionPanier });
        comp.ngOnInit();

        expect(panierService.query).toHaveBeenCalled();
        expect(panierService.addPanierToCollectionIfMissing).toHaveBeenCalledWith(panierCollection, ...additionalPaniers);
        expect(comp.paniersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const sessionPanier: ISessionPanier = { id: 456 };
        const userId: IUser = { id: 20731 };
        sessionPanier.userId = userId;
        const panier: IPanier = { id: 60842 };
        sessionPanier.panier = panier;

        activatedRoute.data = of({ sessionPanier });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sessionPanier));
        expect(comp.usersSharedCollection).toContain(userId);
        expect(comp.paniersSharedCollection).toContain(panier);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SessionPanier>>();
        const sessionPanier = { id: 123 };
        jest.spyOn(sessionPanierService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sessionPanier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sessionPanier }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sessionPanierService.update).toHaveBeenCalledWith(sessionPanier);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SessionPanier>>();
        const sessionPanier = new SessionPanier();
        jest.spyOn(sessionPanierService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sessionPanier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sessionPanier }));
        saveSubject.complete();

        // THEN
        expect(sessionPanierService.create).toHaveBeenCalledWith(sessionPanier);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SessionPanier>>();
        const sessionPanier = { id: 123 };
        jest.spyOn(sessionPanierService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sessionPanier });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sessionPanierService.update).toHaveBeenCalledWith(sessionPanier);
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

      describe('trackPanierById', () => {
        it('Should return tracked Panier primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPanierById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
