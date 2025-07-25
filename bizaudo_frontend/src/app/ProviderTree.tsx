import { MantineProvider } from "@mantine/core";
import type React from "react";
import { GlobalLoadingProvider } from "../providers/GlobalLoadingProvider";
import { AuthProvider } from "../providers/AuthProvider";
import { defaultTheme } from "../config/Theme";
import { ModalsProvider } from "@mantine/modals";
import { Notifications } from "@mantine/notifications";

/* 
A React component that wraps the application in multiple providers.
This is to ensure all the providers are easily accessible and can
be found in the codebase super easily.

@author IFD
@since 2025-06-27
*/
export default function ProviderTree({ child }: { child: React.ReactElement }) {
  return (
    <MantineProvider theme={defaultTheme} defaultColorScheme="auto">
      <GlobalLoadingProvider>
        <AuthProvider>
          <ModalsProvider>
            <Notifications
              limit={5}
              styles={{
                notification: {
                  borderRadius: "0.5rem",
                },
                root: {
                  zIndex: 1000,
                },
              }}
            />
            {child}
          </ModalsProvider>
        </AuthProvider>
      </GlobalLoadingProvider>
    </MantineProvider>
  );
}
