import jwt_decode from "jwt-decode";
import {constant, tokenConstant} from '../constants/constants';

export function getIdFromToken(): number {
  return getFieldFromToken(tokenConstant.TOKEN_ID);
}

export function getFieldFromToken(field: string) {
  const token = getTokenFromStorage();
  const decoded: any = jwt_decode(token!);
  return decoded[field];
}

export function hasRole(role: string): boolean {
  const token = getTokenFromStorage();
  if (token) {
    const decoded: any = jwt_decode(token);
    return decoded.role === role;
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
  getFieldFromToken,
  setTokenInStorage,
  getTokenFromStorage,
  removeTokenFromStorage,
};
export default tokenService;
