import { modals } from "@mantine/modals";
import useProfile from "../../hooks/ProfileHook";
import { useEffect } from "react";
import { Anchor, Box, Button, Text, Title, Space } from "@mantine/core";
import { useNavigate } from "react-router-dom";
import IconLogo from "../../../../components/images/IconLogo";

export default function CompleteProfileModal({
  required = false,
}: {
  required?: boolean;
}) {
  const profile = useProfile();
  const navigate = useNavigate();
  const devForceKey = import.meta.hot?.data?.timestamp ?? Date.now();

  useEffect(() => {
    if (profile && profile.authProfileComplete === false) {
      modals.open({
        closeOnEscape: false,
        closeOnClickOutside: false,
        withCloseButton: false,
        radius: "xl",
        centered: true,
        size: "auto",
        children: (
          <Box
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
            maw={300}
            p={"lg"}
          >
            <IconLogo width={80} height={80} />
            <Space h="md" />
            <Title mb="md" ta="center" lh={1}>
              {required ? "Complete Your Profile" : "Thanks for signing up!"}
            </Title>
            <Text size="sm" ta="center" c={"dimmed"} fs={"italic"}>
              {required
                ? "Please complete your profile to access all features."
                : "We recommend completing your profile to get the most out of our platform."}
            </Text>
            <Space h="md" />
            <Button
              onClick={() => {
                modals.closeAll();
                navigate("/complete-profile");
              }}
            >
              Complete Profile
            </Button>
            <Space h="md" />
            <Anchor
              size="xs"
              variant="text"
              onClick={() => modals.closeAll()}
              c={"dimmed"}
            >
              No thanks, I'll do it later
            </Anchor>
          </Box>
        ),
      });
    } else if (profile === null) {
      modals.closeAll();
    }
  }, [profile, devForceKey, required]);

  // ✅ Always return something after hook calls
  return null;
}
