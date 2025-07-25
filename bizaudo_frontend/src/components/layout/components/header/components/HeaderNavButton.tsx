import { Center, Tooltip, UnstyledButton } from "@mantine/core";
import { IconHome } from "@tabler/icons-react";
import buttonClass from "../../../../../css/buttons/HeaderMenuButtons.module.css";
import React from "react";

export default function HeaderNavButton({
  tooltipText = "Home",
  icon = IconHome,
}: {
  tooltipText?: string;
  icon?: React.ComponentType<{ size?: number; stroke?: number; fill?: string }>;
}) {
  return (
    <Tooltip
      label={tooltipText}
      position="bottom"
      bg={"gray.3"}
      c={"black"}
      py={3}
    >
      <UnstyledButton h={"100%"} w={125} className={buttonClass.button}>
        <Center>
          {React.createElement(icon, {
            size: 30,
            stroke: 1,
            fill: "transparent",
          })}
        </Center>
      </UnstyledButton>
    </Tooltip>
  );
}
