import { ActionIcon, Divider, Group, Space, Text, Image } from "@mantine/core";
import google from "../../../assets/brands/google.svg";
import facebook from "../../../assets/brands/facebook.svg";

export default function ExternalAuthForm() {
  return (
    <>
      <Group justify="center" align="center" grow mt="md">
        <Divider />
        <Text
          size="sm"
          c={"dimmed"}
          ta={"center"}
          style={{ textWrap: "nowrap" }}
        >
          Or continue with
        </Text>
        <Divider />
      </Group>
      <Space h="md" />
      <Group justify="center" align="center">
        <ActionIcon variant="default" size={50}>
          <Image src={google} w={30} h={30} />
        </ActionIcon>
        <ActionIcon variant="default" size={50}>
          <Image src={facebook} w={30} h={30} />
        </ActionIcon>
      </Group>
    </>
  );
}
