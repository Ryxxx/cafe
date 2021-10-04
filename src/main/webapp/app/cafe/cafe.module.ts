import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { cafeState } from './cafe.route';
import { HomeComponent } from './home/home.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(cafeState)],
  declarations: [HomeComponent],
})
export class CafeModule {}
