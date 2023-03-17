import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ThongBaoHuiComponent } from './thongbao-hui.component';

const routes: Routes = [
    {
        path: '',
        component: ThongBaoHuiComponent
    }
]

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ThongBaoHuiRoutingModule { }