import { Avatar, Box, Grid, Group } from "@mantine/core";
import HeaderNavButton from "./components/HeaderNavButton";
import { IconUsers } from "@tabler/icons-react";
import UserAvatarMenu from "../../../../features/user/components/UserAvatarMenu";
import AuthProtectedComponent from "../../../../features/auth/components/AuthProtectedComponent";
import IconLogo from "../../../images/IconLogo";

/* 
A Header component that displays the application header with a 
logo, navigation buttons, and user avatars.

Note that the header is dynamic and will present different 
navigation buttons depending on the users role, account, or
living status.

@author IFD
@since 2025-06-29
*/
export default function Header() {
  return (
    <Box w={"100%"} h={"100%"} px={"sm"}>
      <Grid
        gutter={0}
        h={"100%"}
        styles={{ root: { height: "100%" }, inner: { height: "100%" } }}
      >
        <Grid.Col
          span={"auto"}
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "start",
          }}
        >
          <IconLogo />
        </Grid.Col>
        <Grid.Col
          span={9}
          visibleFrom="md"
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <Group h={"100%"} gap={2}>
            <HeaderNavButton />
            <HeaderNavButton />
            <HeaderNavButton />
            <HeaderNavButton icon={IconUsers} tooltipText="People" />
          </Group>
        </Grid.Col>
        <Grid.Col
          span={"auto"}
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "end",
          }}
        >
          <AuthProtectedComponent>
            <Group>
              <Avatar />
              <Avatar />
              <UserAvatarMenu />
            </Group>
          </AuthProtectedComponent>
        </Grid.Col>
      </Grid>
    </Box>
  );
}
