import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-mat-input',
  templateUrl: './mat-input.component.html',
  styleUrls: ['./mat-input.component.css'],
})
export class MatInputComponent {
  @Input() type = "text";
  @Input() label = '';
  @Input() hint = '';
  @Input() value: any;
  @Input() readonly: boolean = true;
  @Input() maxLenght: number = 255;

  getAppearance() {
    return !this.readonly ? "fill" : "outline";
  }
}
