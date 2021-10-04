import { Route } from '@angular/router';

import { HomeComponent } from './home.component';

export const HomeRoute: Route = {
  path: 'home',
  component: HomeComponent,
  data: {
    pageTitle: 'cafe.home',
  },
};
