import { Group, Menu, Text } from "@mantine/core";
import {
  IconLogout,
  IconMessage2Exclamation,
  IconQuestionMark,
  IconSettings,
  IconSun,
} from "@tabler/icons-react";
import RouteLink from "../../../components/buttons/RouteLink";
import useAuth from "../../../hooks/AuthenticationHook";
import { useNavigate } from "react-router-dom";
import { showErrorNotification } from "../../../lib/utils/NotificationManager";
import { UserAvatar, UserAvatarWithText } from "./UserAvatar";

/* 
A UserAvatarMenu component that displays a user avatar menu.
It includes options for profile settings, help, display settings, 
feedback, and logout.

@author IFD
@date 2025-07-01
*/
export default function UserAvatarMenu() {
  // Import the useAuth hook to access authentication functions
  const { logout } = useAuth();

  // Import the useNavigate hook from react-router-dom to handle navigation
  const navigate = useNavigate();

  /* 
  A function to handle user logout.

  It calls the logout function from the authentication hook and 
  navigates to the auth page on success. If an error occurs 
  during logout, it shows an error notification.

  @author IFD
  @date 2025-07-01
  */
  const handleLogout = async () => {
    await logout()
      .then(() => {
        navigate("/auth?login=true", { replace: true });
      })
      .catch(() => {
        showErrorNotification(
          "Logout Failed",
          "An error occurred while trying to log out. Please try again.",
        );
      });
  };

  return (
    <Menu shadow="md" styles={{ dropdown: { minWidth: 300 } }}>
      <Menu.Target>
        <div>
          <UserAvatar />
        </div>
      </Menu.Target>
      <Menu.Dropdown>
        <Menu.Label px={0} py={0}>
          <UserAvatarWithText />
        </Menu.Label>
        <Menu.Divider />
        <Menu.Item>
          <Group gap={"xs"}>
            <IconSettings size={20} />
            <Text size="sm" lh={1}>
              Profile & settings
            </Text>
          </Group>
        </Menu.Item>
        <Menu.Item>
          <Group gap={"xs"}>
            <IconQuestionMark size={20} />
            <Text size="sm" lh={1}>
              Help & support
            </Text>
          </Group>
        </Menu.Item>
        <Menu.Item>
          <Group gap={"xs"}>
            <IconSun size={20} />
            <Text size="sm" lh={1}>
              Display & accessibility
            </Text>
          </Group>
        </Menu.Item>
        <Menu.Item>
          <RouteLink to="/report">
            <Group gap={"xs"}>
              <IconMessage2Exclamation size={20} />
              <Text size="sm" lh={1}>
                Give feedback
              </Text>
            </Group>
          </RouteLink>
        </Menu.Item>
        <Menu.Divider />
        <Menu.Item color="red" onClick={async () => await handleLogout()}>
          <Group gap={"xs"}>
            <IconLogout size={20} />
            <Text size="sm">Logout</Text>
          </Group>
        </Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
}
