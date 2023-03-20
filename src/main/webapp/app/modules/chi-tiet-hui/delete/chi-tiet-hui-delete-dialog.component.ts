import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChiTietHui } from '../chi-tiet-hui.model';
import { ChiTietHuiService } from '../service/chi-tiet-hui.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './chi-tiet-hui-delete-dialog.component.html',
})
export class ChiTietHuiDeleteDialogComponent {
  chiTietHui?: IChiTietHui;

  constructor(protected chiTietHuiService: ChiTietHuiService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chiTietHuiService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
