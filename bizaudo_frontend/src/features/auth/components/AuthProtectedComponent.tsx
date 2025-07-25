import useAuth from "../../../hooks/AuthenticationHook";

/* 
A React component that conditionally renders its children 
based on the user's authentication status. If the user is 
not authenticated, it returns null, effectively hiding the children.

@author IFD
@since 2025-06-27
*/
export default function AuthProtectedComponent({
  children,
}: {
  children: React.ReactNode;
}) {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return null;
  } else {
    return <>{children}</>;
  }
}
