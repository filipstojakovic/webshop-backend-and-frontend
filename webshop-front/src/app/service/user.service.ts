import {Injectable} from '@angular/core';
import {UserPasswordRequest} from '../model/request/UserPasswordRequest';
import tokenService from './TokenService';
import {backendUrl} from '../constants/backendUrl';
import {HttpClient} from '@angular/common/http';
import {GenericCrudService} from './GenericCrud.service';
import {UserRequest} from '../model/UserRequest';

@Injectable({
  providedIn: 'root',
})
export class UserService extends GenericCrudService<UserRequest> {

  constructor(override http: HttpClient) {
    super(backendUrl.USERS, http);
  }

  changePassword(userPasswordRequest: UserPasswordRequest) {
    const userId = tokenService.getIdFromToken();
    const url = backendUrl.USERS + `/${userId}/` + "change-password";
    return this.http.patch(url, userPasswordRequest);
  }
}
