import {RoleEnum} from './enums/role';

export class User {
  id?: number;
  username: string = "";
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  city: string = "";
  avatarPath: string|null = null;
  role: RoleEnum | null = null;

}
