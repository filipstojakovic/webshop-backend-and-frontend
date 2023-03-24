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
  @Output() keyDown = new EventEmitter();

  onKeyDown(event: KeyboardEvent): void {
    this.keyDown.emit();
  }
}
