import { AppShell } from "@mantine/core";
import Header from "./components/header/Header";
import { Helmet } from "react-helmet-async";

export default function DefaultLayout({
  children,
  pageTitle = "Bizaudo",
  pageDescription = "Bizaudo - Business Management System",
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
      <AppShell header={{ height: 60 }} padding="md">
        <AppShell.Header>
          <Header />
        </AppShell.Header>
        <AppShell.Main>{children}</AppShell.Main>
        <AppShell.Footer></AppShell.Footer>
      </AppShell>
    </>
  );
}
