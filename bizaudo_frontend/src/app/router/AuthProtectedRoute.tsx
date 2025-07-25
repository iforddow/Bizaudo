import { Navigate } from "react-router-dom";
import useAuth from "../../hooks/AuthenticationHook";

export default function AuthProtectedRoute({
  children,
}: {
  children: React.ReactNode;
}) {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to={"/auth"} replace={true} />;
  } else {
    return <>{children}</>;
  }
}
