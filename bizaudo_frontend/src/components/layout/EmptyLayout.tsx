import { AppShell } from "@mantine/core";
import { Helmet } from "react-helmet-async";

export default function EmptyLayout({
  children,
  pageTitle = "Zeedy",
  pageDescription = "Zeedy - Property Management System",
}: {
  children: React.ReactNode;
  pageTitle?: string;
  pageDescription?: string;
}) {
  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
        <meta name="description" content={pageDescription} />
      </Helmet>
      <AppShell>
        <AppShell.Main>{children}</AppShell.Main>
      </AppShell>
    </>
  );
}
