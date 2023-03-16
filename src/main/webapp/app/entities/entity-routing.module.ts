import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'hui',
        data: { pageTitle: 'Huis' },
        loadChildren: () => import('../modules/hui/hui.module').then(m => m.HuiModule),
      },
    ]),
  ],
})
export class EntityRoutingModule {}
