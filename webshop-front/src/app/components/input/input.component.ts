import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css']
})
export class InputComponent {
  @Input() control!: FormControl;
  @Input() type = "text";
  @Input() label = '';
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
    return !this.readonly? "fill" : "outline";
  }
}
