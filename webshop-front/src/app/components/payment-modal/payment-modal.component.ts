import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastService} from 'angular-toastify';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Product} from '../../model/Product';
import {PurchaseRequest} from '../../model/request/PurchaseRequest';
import {PaymentMethod} from '../../model/PaymentMethod';

@Component({
  selector: 'app-payment-modal',
  templateUrl: './payment-modal.component.html',
  styleUrls: ['./payment-modal.component.css'],
})
export class PaymentModalComponent implements OnInit {

  dialogForm: FormGroup = new FormGroup({});
  product: Product;
  currentPaymentMethod: PaymentMethod;

  constructor(
      public fb: FormBuilder,
      public dialogRef: MatDialogRef<PaymentModalComponent>,
      private _toastService: ToastService,
      @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.product = data.dialogData;
  }


  ngOnInit(): void {

    this.dialogForm = this.fb.group({
      paymentMethodId: ["", Validators.required],
      value: ["", Validators.required],
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  purchaseSubmit({ value, valid }: { value: PurchaseRequest, valid: boolean }) {
    console.log("payment-modal.component.ts > saveTask(): " + JSON.stringify(value, null, 2));
    if (!this.dialogForm.valid) {
      this._toastService.error("Form not valid");
      return;
    }
    this.dialogForm.reset();
    this.dialogRef.close(value);
  }

  checkPaymentMethod() {
    if (this.currentPaymentMethod) {
      return this.currentPaymentMethod.name.toLowerCase().includes('card');
    }
    return false;
  }

  selectedPaymentMethod(paymentMethod: PaymentMethod) {
    this.currentPaymentMethod = paymentMethod;
  }
}
