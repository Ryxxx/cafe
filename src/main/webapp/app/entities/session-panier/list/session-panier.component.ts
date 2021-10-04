import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISessionPanier } from '../session-panier.model';
import { SessionPanierService } from '../service/session-panier.service';
import { SessionPanierDeleteDialogComponent } from '../delete/session-panier-delete-dialog.component';

@Component({
  selector: 'jhi-session-panier',
  templateUrl: './session-panier.component.html',
})
export class SessionPanierComponent implements OnInit {
  sessionPaniers?: ISessionPanier[];
  isLoading = false;

  constructor(protected sessionPanierService: SessionPanierService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sessionPanierService.query().subscribe(
      (res: HttpResponse<ISessionPanier[]>) => {
        this.isLoading = false;
        this.sessionPaniers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISessionPanier): number {
    return item.id!;
  }

  delete(sessionPanier: ISessionPanier): void {
    const modalRef = this.modalService.open(SessionPanierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sessionPanier = sessionPanier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
