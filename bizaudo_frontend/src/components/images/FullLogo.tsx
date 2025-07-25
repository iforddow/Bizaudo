import { useComputedColorScheme, Image } from "@mantine/core";

export default function FullLogo({
  height = 50,
  width = "auto",
  fit = "contain",
}: {
  height?: string | number;
  width?: string | number;
  fit?: "contain" | "cover" | "fill" | "none" | "scale-down";
}) {
  const computedColorScheme = useComputedColorScheme("light", {
    getInitialValueInEffect: true,
  });

  switch (computedColorScheme) {
    case "dark":
      return <Image src={"/bizaudoFull.png"} h={height} w={width} fit={fit} />;
    case "light":
      return <Image src={"/bizaudoFull.png"} h={height} w={width} fit={fit} />;
    default:
      return <Image src={"/bizaudoFull.png"} h={height} w={width} fit={fit} />;
  }
}
