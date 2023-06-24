import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ThemePalette} from '@angular/material/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css']
})
export class ButtonComponent {
  @Input() type="button";
  @Input() color: ThemePalette = "primary";

  @Output() btnClick = new EventEmitter();

  onBtnClick() {
    this.btnClick.emit();
  }
}
