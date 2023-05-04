import {User} from './User';

export class UserRequest {
  username: string = "";
  password: string = "";
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  city: string = "";
  avatar: string | null = null;

  constructor() {
  }

}
