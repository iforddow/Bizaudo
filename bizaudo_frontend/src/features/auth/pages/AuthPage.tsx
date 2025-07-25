import {
  Card,
  Center,
  Title,
  Image,
  Text,
  Tabs,
  FloatingIndicator,
  Divider,
  Box,
  LoadingOverlay,
  Anchor,
} from "@mantine/core";
import EmptyLayout from "../../../components/layout/EmptyLayout";
import { useState } from "react";
import classes from "../../../css/tabs/FloatingAuthTabs.module.css";
import { Navigate, useSearchParams } from "react-router-dom";
import LoginForm from "../../../features/auth/components/LoginForm";
import useLoading from "../../../hooks/LoadingHook";
import ExternalAuthForm from "../../../features/auth/components/ExternalAuthForm";
import RegisterForm from "../../../features/auth/components/RegisterForm";
import useAuth from "../../../hooks/AuthenticationHook";

export default function AuthPage() {
  const [searchParams] = useSearchParams();

  const { isAuthenticated } = useAuth();

  const defaultTab = searchParams.get("login") === "true" ? "login" : "signup";

  const { loading, startLoading, stopLoading } = useLoading();

  const [tabListRef, setTabListRef] = useState<HTMLDivElement | null>(null);
  const [tabValue, setTabValue] = useState<string | null>(defaultTab);
  const [tabControlsRefs, setTabControlsRefs] = useState<
    Record<string, HTMLButtonElement | null>
  >({});

  const setTabControlRef = (val: string) => (node: HTMLButtonElement) => {
    tabControlsRefs[val] = node;
    setTabControlsRefs(tabControlsRefs);
  };

  if (isAuthenticated) {
    return <Navigate to={"/"} replace={true} />;
  }

  return (
    <EmptyLayout>
      <Center h={"100vh"}>
        <Box pos={"relative"} miw={300} w={"25%"}>
          <Card p={"xl"} shadow="md" radius={"xxl"} w={"100%"}>
            <LoadingOverlay
              visible={loading}
              zIndex={999}
              overlayProps={{ radius: "sm", blur: 2 }}
            />
            <Center>
              <Image src={"/bizaudoIcon.png"} w={50} />
            </Center>
            <Title ta={"center"} lh={1} mt={"md"} mb={3}>
              bizaudo
            </Title>
            <Text ta={"center"} size="sm" c={"dimmed"}>
              Business management system.
            </Text>
            <Tabs
              variant="none"
              value={tabValue}
              onChange={setTabValue}
              mt={"md"}
            >
              <Tabs.List ref={setTabListRef} className={classes.list} grow>
                <Tabs.Tab
                  value="login"
                  ref={setTabControlRef("login")}
                  className={classes.tab}
                >
                  Log in
                </Tabs.Tab>
                <Tabs.Tab
                  value="signup"
                  ref={setTabControlRef("signup")}
                  className={classes.tab}
                >
                  Sign up
                </Tabs.Tab>
                <FloatingIndicator
                  target={tabValue ? tabControlsRefs[tabValue] : null}
                  parent={tabListRef}
                  className={classes.indicator}
                />
              </Tabs.List>

              <Divider mb={"md"} />

              <Tabs.Panel value="login">
                <LoginForm
                  startLoading={startLoading}
                  stopLoading={stopLoading}
                />
              </Tabs.Panel>
              <Tabs.Panel value="signup" mt="xs">
                <RegisterForm
                  startLoading={startLoading}
                  stopLoading={stopLoading}
                />
              </Tabs.Panel>
            </Tabs>
            <ExternalAuthForm />
            <Text mt="md" size="xs" c={"dimmed"} ta={"center"}>
              By using this application, you acknowledge and agree to be bound
              by our{" "}
              <Anchor size="xs" href={"/terms"} underline="hover">
                Terms of Service
              </Anchor>{" "}
              and{" "}
              <Anchor size="xs" href={"/privacy"} underline="hover">
                Privacy Policy
              </Anchor>
              .
            </Text>
          </Card>
        </Box>
      </Center>
    </EmptyLayout>
  );
}
