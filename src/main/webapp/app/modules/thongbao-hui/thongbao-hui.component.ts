import { Component, Input } from '@angular/core';

@Component({
    selector: 'jhi-thongbao-hui',
    templateUrl: './thongbao-hui.component.html',
    styleUrls: ['./thongbao-hui.component.scss']
})
export class ThongBaoHuiComponent {
    isLoading = false;

    constructor() {
        console.log( '123' );
    }
}