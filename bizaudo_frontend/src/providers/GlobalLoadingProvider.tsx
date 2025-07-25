import { useState, type ReactNode } from "react";
import type { GlobalLoadingContextType } from "../lib/types/context_types/GlobalLoadingContextType";
import { GlobalLoadingContext } from "../context/LoadingContext";
import { Box, Loader, LoadingOverlay } from "@mantine/core";

/* 
A React provider component that manages global loading state.
This component provides a context for managing loading state across the application,
allowing components to show or hide loading indicators as needed.

@author IFD
@since 2025-06-27
*/
export const GlobalLoadingProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  // This hook is used to manage loading state in components.
  // It provides a simple way to show and hide loading indicators.
  const [globalLoading, setGlobalLoading] = useState(true);

  /* 
  A context value that provides the loading state and functions
  to manage it. This value is passed down to components
  that need to access or modify the loading state.

  @author IFD
  @date 2025-06-15
  */
  const value: GlobalLoadingContextType = {
    globalLoading,
    setGlobalLoading,
  };

  return (
    <GlobalLoadingContext.Provider value={value}>
      <LoadingOverlay
        visible={globalLoading}
        zIndex={1000}
        overlayProps={{ blur: 1 }}
        loaderProps={{
          children: (
            <Box style={{ zIndex: 1001 }}>
              <Loader />
            </Box>
          ),
        }}
      />
      {children}
    </GlobalLoadingContext.Provider>
  );
};
