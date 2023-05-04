import {User} from '../User';
import {UserRequest} from '../UserRequest';

function mapFromUser(user: User): UserRequest {
  const userRequest = new UserRequest();
  userRequest.username = user.username;
  userRequest.firstName = user.firstName;
  userRequest.lastName = user.lastName;
  userRequest.email = user.email;
  userRequest.city = user.city;
  userRequest.avatar = user.avatarPath;
  return userRequest;
}

const userMapper = {
  mapFromUser
}

export default userMapper;
