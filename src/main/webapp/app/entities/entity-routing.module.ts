import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'hui',
        data: { pageTitle: 'Huis' },
        loadChildren: () => import('./hui/hui.module').then(m => m.HuiModule),
      },
      {
        path: 'hui-vien',
        data: { pageTitle: 'HuiViens' },
        loadChildren: () => import('./hui-vien/hui-vien.module').then(m => m.HuiVienModule),
      },
      {
        path: 'chi-tiet-hui',
        data: { pageTitle: 'ChiTietHuis' },
        loadChildren: () => import('./chi-tiet-hui/chi-tiet-hui.module').then(m => m.ChiTietHuiModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
