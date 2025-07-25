import { createContext } from "react";
import { type GlobalLoadingContextType } from "../lib/types/context_types/GlobalLoadingContextType";

/* 
A React context for managing global loading state.
This context provides methods and state related to global loading,
such as showing and hiding a global loading indicator, and 
checking if the application is currently loading.

@author IFD
@since 2025-06-27
*/
export const GlobalLoadingContext = createContext<
  GlobalLoadingContextType | undefined
>(undefined);
