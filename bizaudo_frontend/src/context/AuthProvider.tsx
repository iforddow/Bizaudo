import { createContext } from "react";
import { type AuthContextType } from "../lib/types/context_types/AuthContextType";

/* 
A React context for managing authentication state.
This context provides methods and state related to user authentication,
such as login, logout, and checking if a user is authenticated.

@author IFD
@since 2025-06-27
*/
export const AuthContext = createContext<AuthContextType | undefined>(
  undefined,
);
