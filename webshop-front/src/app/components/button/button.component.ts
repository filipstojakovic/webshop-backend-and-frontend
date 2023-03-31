import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css']
})
export class ButtonComponent {
  @Input() color!: string;
  @Input() type="button";


  @Output() btnClick = new EventEmitter();

  onBtnClick() {
    this.btnClick.emit();
  }
}
