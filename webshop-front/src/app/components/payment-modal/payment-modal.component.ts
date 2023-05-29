import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastService} from 'angular-toastify';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {PurchaseRequest} from '../../model/request/PurchaseRequest';
import {Product} from '../../model/Product';

@Component({
  selector: 'app-payment-modal',
  templateUrl: './payment-modal.component.html',
  styleUrls: ['./payment-modal.component.css'],
})
export class PaymentModalComponent implements OnInit {

  dialogForm: FormGroup = new FormGroup({});
  product: Product;


  constructor(
      public fb: FormBuilder,
      public dialogRef: MatDialogRef<PaymentModalComponent>,
      private _toastService: ToastService,
      @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    console.log("payment-modal.component.ts > constructor(): " + JSON.stringify(data, null, 2));
    this.product = data.dialogData;
  }


  ngOnInit(): void {
    this.dialogForm = this.fb.group({
      productName: this.product.name,
      cardNumber: ["", Validators.required],
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  saveTask({ value, valid }: { value: PurchaseRequest, valid: boolean }) {
    if (!this.dialogForm.valid) {
      this._toastService.error("form not valid");
      return;
    }
    console.log("payment-modal.component.ts > saveTask(): " + JSON.stringify(value, null, 2));
    this.dialogForm.reset();
    this.dialogRef.close(value);
  }
}
