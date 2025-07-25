import { Avatar, Group, Text, UnstyledButton } from "@mantine/core";
import { shortenEmail } from "../functions/UserFunctions";
import useAuth from "../../../hooks/AuthenticationHook";
import userAvatarClasses from "../../../css/buttons/UserAvatarButton.module.css";

/* 
A UserAvatar component that displays the user's avatar.
It shows the users first two letters of their email address
as initials in a styled avatar.

@author IFD
@since 2025-07-01
*/
export function UserAvatar() {
  const { user } = useAuth();

  return (
    <Avatar
      style={{
        cursor: "pointer",
      }}
      bg={"primary.3"}
    >
      <Text lh={1} c={"primary.9"} fw={700}>
        {shortenEmail(user?.email)}
      </Text>
    </Avatar>
  );
}

/* 
A UserAvatarWithText component that displays the user's avatar
and their name or email address.

@author IFD
@since 2025-07-01
*/
export function UserAvatarWithText() {
  const { user } = useAuth();

  return (
    <UnstyledButton w={"100%"} className={userAvatarClasses.user_avatar_btn}>
      <Group style={{ cursor: "pointer" }} px={0} gap={"sm"}>
        <Avatar bg={"primary.3"}>
          <Text lh={1} c={"primary.9"} fw={700}>
            {shortenEmail(user?.email)}
          </Text>
        </Avatar>
        <Text size="sm" c={"var(--mantine-color-text"}>
          {user?.firstName && user?.lastName
            ? `${user.firstName} ${user.lastName}`
            : user?.email}
        </Text>
      </Group>
    </UnstyledButton>
  );
}
