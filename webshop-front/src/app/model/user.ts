import {RoleEnum} from './role';

export class User {
  id?: number;
  username: string = "";
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  city: string = "";
  avatar: string = "";
  role: RoleEnum | null = null;
}
