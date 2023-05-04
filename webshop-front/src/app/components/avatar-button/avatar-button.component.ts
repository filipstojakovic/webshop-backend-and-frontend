import {Component, EventEmitter, Input, Output} from '@angular/core';
import {User} from '../../model/User';

@Component({
  selector: 'app-avatar-button',
  templateUrl: './avatar-button.component.html',
  styleUrls: ['./avatar-button.component.css'],
})
export class AvatarButtonComponent {
  @Input() id: number | null = null;
  @Output() click = new EventEmitter();

  imageClick() {
    console.log("avatar-button.component.ts > imageClick(): " + "image clicked");
    this.click.emit();
  }
}
