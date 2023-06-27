import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PaymentService} from '../../service/payment.service';
import {PaymentMethod} from '../../model/PaymentMethod';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-payment-method-dropdown',
  templateUrl: './payment-method-dropdown.component.html',
  styleUrls: ['./payment-method-dropdown.component.css'],
})
export class PaymentMethodDropdownComponent implements OnInit {
  @Input() selectedPaymentMethodIdControl: FormControl;
  @Output() paymentMethodChangeEvent = new EventEmitter();

  paymentMethods: PaymentMethod[] = [];

  constructor(private paymentService: PaymentService) {
  }

  ngOnInit(): void {
    this.paymentService.getPaymentMethods().subscribe({
          next: (res) => {
            this.paymentMethods = res;
          },
        },
    )
  }

  onSelectionChange(event: any) {
    const paymentMethodId = event.value;
    const paymentMethod = this.paymentMethods.find(x => x.id === paymentMethodId);
    this.paymentMethodChangeEvent.emit(paymentMethod);

  }
}
