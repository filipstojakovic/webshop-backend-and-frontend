import {Component, Input} from '@angular/core';
import {baseUrl} from '../../constants/backendUrl';

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

  protected readonly baseUrl = baseUrl;
}
