import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-form-input',
  templateUrl: './form-input.component.html',
  styleUrls: ['./form-input.component.css'],
})
export class FormInputComponent {
  @Input() control!: FormControl;
  @Input() type = "text";
  @Input() label = '';
  @Input() hint = '';
  @Input() errorMessage = 'Field is required';
  @Input() required: boolean = false;
  @Input() readonly: boolean = false;
  @Input() maxLenght: number = 255;

  @Output() keyDown = new EventEmitter();

  onKeyDown(): void {
    this.keyDown.emit();
  }

  displayErrors() {
    const { touched, errors } = this.control;
    return (touched && errors) || !this.control.valid;
  }

  getAppearance() {
    return !this.readonly ? "fill" : "outline";
  }
}
