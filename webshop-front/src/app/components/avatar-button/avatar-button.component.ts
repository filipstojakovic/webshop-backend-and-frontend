import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-avatar-button',
  templateUrl: './avatar-button.component.html',
  styleUrls: ['./avatar-button.component.css']
})
export class AvatarButtonComponent {
  @Input() image:any;
  @Output() click = new EventEmitter();

  imageClick() {
    console.log("avatar-button.component.ts > imageClick(): "+ "image clicked");
    this.click.emit();
  }
}
