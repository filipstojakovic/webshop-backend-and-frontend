export const constant = {
  TOKEN_STORAGE: "token",
  USER_TIME_FORMAT: 'H:m',
  USER_DATE_FORMAT: 'dd/MM/yyyy',
  MAX_NUM_OF_CHAR: 255,
  PIN_DIGIT_NUM: 4,
  MAX_FILE_SIZE: 5_000_000,
} as const;

export const tokenConstant = {
  TOKEN_ID: "userID",
  IS_ACTIVE: "isActive",
  ROLE: "role",
  USERNAME: "sub",
}
