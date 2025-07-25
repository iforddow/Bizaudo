import {
  ActionIcon,
  useComputedColorScheme,
  useMantineColorScheme,
} from "@mantine/core";
import { IconMoon, IconSun } from "@tabler/icons-react";

/* 
A button component that toggles between light and dark themes.
It uses Mantine's color scheme management to switch themes and displays
an icon representing the current theme (sun for light, moon for dark).

Props:
- buttonSize: Size of the button (default: "md").
- iconSize: Size of the icon (default: 20).
- buttonColor: Background color of the button (default: "primary").
- buttonVariant: Variant of the button (default: "filled").
- buttonRadius: Border radius of the button (default: "md").
- iconColor: Color of the icon (default: "white").

@author IFD
@since 2025-06-28
*/
export default function ThemeToggleButton({
  buttonSize = "md",
  iconSize = 20,
  buttonColor = "primary",
  buttonVariant = "filled",
  buttonRadius = "md",
  iconColor = "white",
}: {
  buttonSize?: "sm" | "md" | "lg";
  iconSize?: number;
  buttonColor?: string;
  buttonVariant?: "filled" | "light" | "outline";
  buttonRadius?: "xs" | "sm" | "md" | "lg" | "xl";
  iconColor?: string;
}) {
  // Use Mantine's hooks to manage the color scheme
  const { setColorScheme } = useMantineColorScheme();

  // Use a computed color scheme to determine the current theme
  const computedColorScheme = useComputedColorScheme("light", {
    getInitialValueInEffect: true,
  });

  // Determine the active icon based on the current color scheme
  const ActiveIcon = () => {
    switch (computedColorScheme) {
      case "dark":
        return <IconMoon size={iconSize} stroke={1.5} color={iconColor} />;
      case "light":
        return <IconSun size={iconSize} stroke={1.5} color={iconColor} />;
    }
  };

  // Function to toggle the color scheme between light and dark
  const toggleColorScheme = () => {
    switch (computedColorScheme) {
      case "dark":
        setColorScheme("light");
        break;
      case "light":
        setColorScheme("dark");
        break;
      default:
        setColorScheme("light");
        break;
    }
  };

  return (
    <ActionIcon
      size={buttonSize}
      aria-label="Toggle theme"
      onClick={toggleColorScheme}
      bg={buttonColor}
      variant={buttonVariant}
      radius={buttonRadius}
    >
      {ActiveIcon()}
    </ActionIcon>
  );
}
