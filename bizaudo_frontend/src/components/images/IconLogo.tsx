import { Image } from "@mantine/core";

export default function IconLogo({
  height = 35,
  width = "auto",
  fit = "contain",
}: {
  height?: string | number;
  width?: string | number;
  fit?: "contain" | "cover" | "fill" | "none" | "scale-down";
}) {
  return <Image src={"/bizaudoIcon.png"} h={height} w={width} fit={fit} />;
}
