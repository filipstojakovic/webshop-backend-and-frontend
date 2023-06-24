export const backendUrl = {
  AUTHENTICATE: "login",
  USERS: "users",
  WHOAMI: "user/me",
  REGISTER: 'register',
  PIN: 'pins',
  PRODUCTS: 'products',
  PRODUCTS_SEARCH: 'products/search',
  PRODUCTS_USER_SEARCH: "products/user/search",
  PRODUCT_PURCHASE_HISTORY_SEARCH: "products/purchase-history/search",
  CATEGORIES: 'categories',
  PURCHASES: 'purchases',
  PAYMENT_METHODS: "payment-methods",

} as const;

export const baseUrl = 'http://localhost:8080';
