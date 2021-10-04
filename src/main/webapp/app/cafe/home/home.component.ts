import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'jhi-cafehome',
  templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {

  constructor (private route: ActivatedRoute) {}

  ngOnInit(): void {
    const error = false;
  }
}
