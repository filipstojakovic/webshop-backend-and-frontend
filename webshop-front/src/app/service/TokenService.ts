import jwt_decode from "jwt-decode";
import {constant} from '../constants/constants';

export function getIdFromToken(): number {
  const token = getTokenFromStorage();
  const decoded: any = jwt_decode(token!);
  return decoded[constant.TOKEN_ID];
}

export function hasRole(role: string): boolean {
  const token = getTokenFromStorage();
  if (token) {
    const decoded: any = jwt_decode(token);
    if (decoded.roles.find((tokenRole: any) => tokenRole === role))
      return true;
  }
  return false;
}

export function setTokenInStorage(token: string): void {
  sessionStorage.setItem(constant.TOKEN_STORAGE, token);
}

export function getTokenFromStorage(): string | null {
  return sessionStorage.getItem(constant.TOKEN_STORAGE);
}

export function removeTokenFromStorage(): void {
  sessionStorage.removeItem(constant.TOKEN_STORAGE);
}

const tokenService = {
  getIdFromToken,
  hasRole,
  setTokenInStorage,
  getTokenFromStorage,
  removeTokenFromStorage,
};
export default tokenService;
