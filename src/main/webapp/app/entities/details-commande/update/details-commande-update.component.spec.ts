jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DetailsCommandeService } from '../service/details-commande.service';
import { IDetailsCommande, DetailsCommande } from '../details-commande.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPaiement } from 'app/entities/paiement/paiement.model';
import { PaiementService } from 'app/entities/paiement/service/paiement.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';

import { DetailsCommandeUpdateComponent } from './details-commande-update.component';

describe('Component Tests', () => {
  describe('DetailsCommande Management Update Component', () => {
    let comp: DetailsCommandeUpdateComponent;
    let fixture: ComponentFixture<DetailsCommandeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let detailsCommandeService: DetailsCommandeService;
    let userService: UserService;
    let paiementService: PaiementService;
    let commandeService: CommandeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DetailsCommandeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DetailsCommandeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DetailsCommandeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      detailsCommandeService = TestBed.inject(DetailsCommandeService);
      userService = TestBed.inject(UserService);
      paiementService = TestBed.inject(PaiementService);
      commandeService = TestBed.inject(CommandeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const detailsCommande: IDetailsCommande = { id: 456 };
        const userId: IUser = { id: 69369 };
        detailsCommande.userId = userId;

        const userCollection: IUser[] = [{ id: 85197 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [userId];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ detailsCommande });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call paimentId query and add missing value', () => {
        const detailsCommande: IDetailsCommande = { id: 456 };
        const paimentId: IPaiement = { id: 98191 };
        detailsCommande.paimentId = paimentId;

        const paimentIdCollection: IPaiement[] = [{ id: 85816 }];
        jest.spyOn(paiementService, 'query').mockReturnValue(of(new HttpResponse({ body: paimentIdCollection })));
        const expectedCollection: IPaiement[] = [paimentId, ...paimentIdCollection];
        jest.spyOn(paiementService, 'addPaiementToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ detailsCommande });
        comp.ngOnInit();

        expect(paiementService.query).toHaveBeenCalled();
        expect(paiementService.addPaiementToCollectionIfMissing).toHaveBeenCalledWith(paimentIdCollection, paimentId);
        expect(comp.paimentIdsCollection).toEqual(expectedCollection);
      });

      it('Should call Commande query and add missing value', () => {
        const detailsCommande: IDetailsCommande = { id: 456 };
        const commande: ICommande = { id: 49633 };
        detailsCommande.commande = commande;

        const commandeCollection: ICommande[] = [{ id: 48031 }];
        jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
        const additionalCommandes = [commande];
        const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
        jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ detailsCommande });
        comp.ngOnInit();

        expect(commandeService.query).toHaveBeenCalled();
        expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(commandeCollection, ...additionalCommandes);
        expect(comp.commandesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const detailsCommande: IDetailsCommande = { id: 456 };
        const userId: IUser = { id: 94726 };
        detailsCommande.userId = userId;
        const paimentId: IPaiement = { id: 65525 };
        detailsCommande.paimentId = paimentId;
        const commande: ICommande = { id: 54797 };
        detailsCommande.commande = commande;

        activatedRoute.data = of({ detailsCommande });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(detailsCommande));
        expect(comp.usersSharedCollection).toContain(userId);
        expect(comp.paimentIdsCollection).toContain(paimentId);
        expect(comp.commandesSharedCollection).toContain(commande);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DetailsCommande>>();
        const detailsCommande = { id: 123 };
        jest.spyOn(detailsCommandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ detailsCommande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detailsCommande }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(detailsCommandeService.update).toHaveBeenCalledWith(detailsCommande);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DetailsCommande>>();
        const detailsCommande = new DetailsCommande();
        jest.spyOn(detailsCommandeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ detailsCommande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: detailsCommande }));
        saveSubject.complete();

        // THEN
        expect(detailsCommandeService.create).toHaveBeenCalledWith(detailsCommande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DetailsCommande>>();
        const detailsCommande = { id: 123 };
        jest.spyOn(detailsCommandeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ detailsCommande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(detailsCommandeService.update).toHaveBeenCalledWith(detailsCommande);
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

      describe('trackPaiementById', () => {
        it('Should return tracked Paiement primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPaiementById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCommandeById', () => {
        it('Should return tracked Commande primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommandeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
