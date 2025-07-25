import { useEffect, useState } from "react";
import useAuth from "../../../hooks/AuthenticationHook";

export default function useProfile() {
  const { user, isAuthenticated } = useAuth();

  const [authProfileComplete, setAuthProfileComplete] = useState<
    boolean | null
  >(null);

  useEffect(() => {
    if (!isAuthenticated || !user) {
      setAuthProfileComplete(null);
      return;
    }
    setAuthProfileComplete(user.profileSetup);
  }, [user, isAuthenticated]);

  // Return null if not authenticated
  if (!isAuthenticated) return null;

  return { authProfileComplete };
}
