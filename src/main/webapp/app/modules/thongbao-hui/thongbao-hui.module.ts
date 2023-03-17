import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';

import { ThongBaoHuiRoutingModule } from './thongbao-hui-routing.module';
import { ThongBaoHuiComponent } from './thongbao-hui.component';

@NgModule({
    imports: [
        ThongBaoHuiRoutingModule
    ],
    declarations: [
        ThongBaoHuiComponent
    ],
    schemas: [
        CUSTOM_ELEMENTS_SCHEMA
    ]
})
export class ThongBaoHuiModule { }