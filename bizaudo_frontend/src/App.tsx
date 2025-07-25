import "@mantine/core/styles.css";
import "@mantine/notifications/styles.css";
import "@mantine/dropzone/styles.css";
import "./css/global.css";
import AppRouter from "./app/router/AppRouter";
import ProviderTree from "./app/ProviderTree";

/* 
The main entry point of the application.
This component wraps the application router in a provider tree, which includes
various context providers such as GlobalLoadingProvider and AuthProvider.

The app router defines the main routing structure of the application,
allowing navigation between different pages. It will always be responsible
for rendering the correct page based on the current URL.

@author IFD
@since 2025-06-27
*/
export default function App() {
  return <ProviderTree child={<AppRouter />} />;
}