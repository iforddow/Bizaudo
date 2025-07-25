import { useContext } from "react";
import { GlobalLoadingContext } from "../context/LoadingContext";

/* 
A custom hook that provides access to the global loading context.
This hook allows components to easily access and manipulate the 
global loading state, such as showing or hiding a global loading 
indicator, and checking if the application is currently loading.

@author IFD
@since 2025-06-27
*/
export const useGlobalLoading = () => {
  const context = useContext(GlobalLoadingContext);

  // Ensure that the context is defined, otherwise throw an error.
  if (!context)
    throw new Error("useLoading must be used within a LoadingProvider");
  return context;
};
