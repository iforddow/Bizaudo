import { notifications } from "@mantine/notifications";
import errorClasses from "../../css/notifications/ErrorNotification.module.css";
import successClasses from "../../css/notifications/SuccessNotification.module.css";
import warningClasses from "../../css/notifications/WarningNotification.module.css";
import infoClasses from "../../css/notifications/InfoNotification.module.css";
import {
  IconAlertHexagon,
  IconExclamationCircle,
  IconInfoCircle,
  IconProgressCheck,
} from "@tabler/icons-react";

/* 
A method to show error notifications using Mantine's notifications system.

@param title - The title of the notification. Defaults to "Error".
@param message - The message of the notification. Defaults to a generic error message.
@param autoClose - The duration in milliseconds after which the notification will automatically close. Defaults to 5000ms (5 seconds).

@author IFD
@since 2025-06-27
*/
export function showErrorNotification(
  title?: string,
  message?: string,
  autoClose?: number,
) {
  notifications.show({
    title: title ?? "Error",
    message: message ?? "An error occurred. Please try again later.",
    autoClose: autoClose ?? 5000,
    classNames: {
      root: errorClasses.root,
      title: errorClasses.title,
      closeButton: errorClasses.closeButton,
      icon: errorClasses.icon,
      body: errorClasses.body,
      description: errorClasses.description,
    },
    icon: <IconExclamationCircle stroke={1.5} color="red" size={"32px"} />,
    closeButtonProps: { "aria-label": "Close notification" },
  });
}

/* 
A method to show success notifications using Mantine's notifications system.

@param title - The title of the notification. Defaults to "Success".
@param message - The message of the notification. Defaults to a generic success message.
@param autoClose - The duration in milliseconds after which the notification will automatically close. Defaults to 5000ms (5 seconds).

@author IFD
@since 2025-06-27
*/
export function showSuccessNotification(
  title?: string,
  message?: string,
  autoClose?: number,
) {
  notifications.show({
    title: title ?? "Success",
    message: message ?? "Operation completed successfully.",
    autoClose: autoClose ?? 5000,
    classNames: {
      root: successClasses.root,
      title: successClasses.title,
      closeButton: successClasses.closeButton,
      icon: successClasses.icon,
      body: successClasses.body,
      description: successClasses.description,
    },
    icon: <IconProgressCheck stroke={1.5} color="green" size={"32px"} />,
    closeButtonProps: { "aria-label": "Close notification" },
  });
}

/* 
A method to show warning notifications using Mantine's notifications system.

@param title - The title of the notification. Defaults to "Warning".
@param message - The message of the notification. Defaults to a generic warning message.
@param autoClose - The duration in milliseconds after which the notification will automatically close. Defaults to 5000ms (5 seconds).

@author IFD
@since 2025-06-27
*/
export function showWarningNotification(
  title?: string,
  message?: string,
  autoClose?: number,
) {
  notifications.show({
    title: title ?? "Warning",
    message: message ?? "Please be cautious with this operation.",
    autoClose: autoClose ?? 5000,
    classNames: {
      root: warningClasses.root,
      title: warningClasses.title,
      closeButton: warningClasses.closeButton,
      icon: warningClasses.icon,
      body: warningClasses.body,
      description: warningClasses.description,
    },
    icon: <IconAlertHexagon stroke={1.5} color="orange" size={"32px"} />,
    closeButtonProps: { "aria-label": "Close notification" },
  });
}

/* 
A method to show informational notifications using Mantine's notifications system.

@param title - The title of the notification. Defaults to "Info".
@param message - The message of the notification. Defaults to a generic informational message.
@param autoClose - The duration in milliseconds after which the notification will automatically close. Defaults to 5000ms (5 seconds).

@author IFD
@since 2025-06-27
*/
export function showInfoNotification(
  title?: string,
  message?: string,
  autoClose?: number,
) {
  notifications.show({
    title: title ?? "Info",
    message: message ?? "This is an informational message.",
    autoClose: autoClose ?? 5000,
    classNames: {
      root: infoClasses.root,
      title: infoClasses.title,
      closeButton: infoClasses.closeButton,
      icon: infoClasses.icon,
      body: infoClasses.body,
      description: infoClasses.description,
    },
    icon: (
      <IconInfoCircle
        stroke={1.5}
        color={"var(--mantine-color-blue-5)"}
        size={"32px"}
      />
    ),
    closeButtonProps: { "aria-label": "Close notification" },
  });
}
