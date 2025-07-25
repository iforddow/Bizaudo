import { useState } from "react";

/* 
A custom hook for managing loading state within a component.
Note for global loading, use the GlobalLoadingContext instead.
Compared to GlobalLoadingContext, loading state is not 
persisted across page reloads.

@author IFD
@since 2025-06-26
*/
export default function useLoading() {
  const [loading, setLoading] = useState(false);

  const startLoading = () => setLoading(true);
  const stopLoading = () => setLoading(false);

  return {
    loading,
    startLoading,
    stopLoading,
  };
}
