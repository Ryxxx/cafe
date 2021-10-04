import { Routes } from '@angular/router';

import { HomeRoute } from './home/home.route';


const CAFE_ROUTES = [HomeRoute];

export const cafeState : Routes = [
  {
    path: '',
    children: CAFE_ROUTES,
  },
];
