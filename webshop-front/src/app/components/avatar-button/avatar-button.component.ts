import {Component, EventEmitter, Input, Output} from '@angular/core';
import {User} from '../../model/User';

@Component({
  selector: 'app-avatar-button',
  templateUrl: './avatar-button.component.html',
  styleUrls: ['./avatar-button.component.css'],
})
export class AvatarButtonComponent {
  @Input() id: number | null = null;
  showFallbackImage = false;

  onImageError() {
    this.showFallbackImage = true;
  }
}
