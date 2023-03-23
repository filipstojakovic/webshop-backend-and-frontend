import jwt_decode from 'jwt-decode';

export function decodeToken(token: string) {
  return jwt_decode(token);
}

export function getIdFromToken(token: string): number {
  const decoded: any = decodeToken(token);
  return decoded.id;
}
