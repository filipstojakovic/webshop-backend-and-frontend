import {EventEmitter, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoginEmitterService {

  public loggedInEvent = new EventEmitter();

  constructor() { }
}
