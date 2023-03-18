import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHuiVien } from '../hui-vien.model';
import { HuiVienService } from '../service/hui-vien.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './hui-vien-delete-dialog.component.html',
})
export class HuiVienDeleteDialogComponent {
  huiVien?: IHuiVien;

  constructor(protected huiVienService: HuiVienService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.huiVienService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
