import {RoleEnum} from './enums/role';

export class User {
  id?: number;
  username: string = "";
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  city: string = "";
  avatarPath: string = "";
  role: RoleEnum | null = null;

  constructor(data: any) {
    this.id = data.id;
    this.username = data.username;
    this.firstName = data.firstName;
    this.lastName = data.lastName;
    this.email = data.email;
    this.city = data.city;
    this.avatarPath = data.avatarPath;
    this.role = data.role.name;
  }
}
