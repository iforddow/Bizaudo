export const BIZ_API_BASE_URL = import.meta.env.VITE_AUTH_API_BASE_URL;
export const BIZ_IMAGE_BASE_URL = import.meta.env.VITE_AUTH_BASE_IMAGES_URL;

/* 
A collection of API endpoints for the Business Management application.

@author IFD
@since 2025-06-27
*/
export const PM_API_ENDPOINTS = {
  AUTH: {
    LOGIN: `${BIZ_API_BASE_URL}/auth/login`,
    REGISTER: `${BIZ_API_BASE_URL}/auth/register`,
    LOGOUT: `${BIZ_API_BASE_URL}/auth/logout`,
    REFRESH: `${BIZ_API_BASE_URL}/auth/refresh`,
  },
};
