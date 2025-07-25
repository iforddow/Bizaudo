import type { UserType } from "../entity_types/UserType";

/* 
A TypeScript interface for the authentication context.

@author IFD
@date 2025-06-15
*/
export interface AuthContextType {
  user: UserType | null;
  isAuthenticated: boolean;
  accessToken: string | null;
  login: (email: string, password: string) => Promise<void>;
  register: (
    email: string,
    password: string,
    confirmPassword: string,
  ) => Promise<void>;
  logout: () => Promise<void>;
  refresh: () => Promise<boolean>;
}
