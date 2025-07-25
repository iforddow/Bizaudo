import { Box, Button, Center, Container, Group, Text } from "@mantine/core";
import useAuth from "../../../hooks/AuthenticationHook";
import { useMatch, useNavigate } from "react-router-dom";

/* 
A banner component that displays a message 
and buttons for signing up or logging in.
It is only visible when the user is not 
authenticated.

@author IFD
@since 2025-06-28
*/
export default function AuthBanner() {
  const { isAuthenticated } = useAuth();

  const navigate = useNavigate();

  const isOnAuthPage = useMatch("/auth");

  if (isAuthenticated || isOnAuthPage) return null;

  return (
    <Box
      style={{
        zIndex: 900,
        position: "fixed",
        bottom: 0,
        borderTopLeftRadius: "var(--mantine-radius-lg)",
        borderTopRightRadius: "var(--mantine-radius-lg)",
      }}
      w={"100%"}
      bg={"primary.9"}
    >
      <Container>
        <Center>
          <Box py={"lg"}>
            <Text ta={"center"} c={"white"}>
              This is a demo application. You can log in with the username
              <strong> demo</strong> and password <strong>demo</strong>.
            </Text>
            <Group justify="center" mt="md" w="100%" grow>
              <Button bg="dark.6" c={"white"} onClick={() => navigate("/auth")}>
                Sign up
              </Button>
              <Button
                variant="light"
                c={"white"}
                onClick={() => navigate("/auth?login=true")}
              >
                Log in
              </Button>
            </Group>
          </Box>
        </Center>
      </Container>
    </Box>
  );
}
