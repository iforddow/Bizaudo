import { Button, Title } from "@mantine/core";
import {
  showErrorNotification,
  showInfoNotification,
  showSuccessNotification,
  showWarningNotification,
} from "../../lib/utils/NotificationManager";
import ThemeToggleButton from "../../features/theme/components/ThemeToggleButton";
import DefaultLayout from "../../components/layout/DefaultLayout";

/* 
The HomePage component serves as the main entry point of the application.

@author IFD
@since 2025-06-27
*/
export default function HomePage() {
  return (
    <DefaultLayout>
      <Title>Welcome to the Home Page</Title>
      <p>This is the main entry point of the application.</p>
      <Button
        variant="gradient"
        onClick={() => {
          showErrorNotification();
          showSuccessNotification();
          showWarningNotification();
          showInfoNotification();
        }}
      >
        Hello
      </Button>
      <ThemeToggleButton />
    </DefaultLayout>
  );
}
