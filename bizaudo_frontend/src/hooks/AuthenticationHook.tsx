import { useContext } from "react";
import { AuthContext } from "../context/AuthProvider";

/* 
A custom hook to access the authentication context.
It throws an error if used outside of the AuthProvider.

@author IFD
@date 2025-06-15
*/
export default function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within an AuthProvider");
  return context;
}
