import axios from "axios";
import { PM_API_ENDPOINTS } from "../config/Api_Config";
import type { AuthContextType } from "../lib/types/context_types/AuthContextType";
import { AuthContext } from "../context/AuthProvider";
import { useEffect, useRef, useState, type ReactNode } from "react";
import type { UserType } from "../lib/types/entity_types/UserType";
import { useGlobalLoading } from "../hooks/GlobalLoadingHook";
import { jwtDecode } from "jwt-decode";

/* 
A React context provider for managing authentication state.
It provides user information, authentication status,
access token, and functions for logging in, logging out, 
and refreshing the access token. 

@author IFD
@date 2025-06-15
*/
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  // State to hold user information and authentication status
  const [user, setUser] = useState<UserType | null>(null);
  // Access token state
  const [accessToken, setAccessToken] = useState<string | null>(null);
  // Set the access token in axios defaults if it exists
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  // Ref to hold the timeout for refreshing the access token
  const refreshTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const { setGlobalLoading } = useGlobalLoading();

  /* 
  Clear the refresh timeout if it exists.

  @author IFD
  @date 2025-06-15
  */
  const clearRefreshTimeout = () => {
    if (refreshTimeoutRef.current) {
      clearTimeout(refreshTimeoutRef.current);
      refreshTimeoutRef.current = null;
    }
  };

  /* 
  A scheduleRefresh function that sets a timeout to refresh the access token
  when it is about to expire. It calculates the time remaining until expiration
  and sets a timeout to call the refresh function shortly before expiration.

  @param token - The JWT access token to decode and check expiration.

  @author IFD
  @date 2025-06-15
  */
  const scheduleRefresh = (token: string) => {
    try {
      const decoded: { exp: number } = jwtDecode(token);
      const expiresAt = decoded.exp * 1000;
      const now = Date.now();
      const timeout = Math.max(expiresAt - now - 60_000, 0);
      clearRefreshTimeout();
      refreshTimeoutRef.current = setTimeout(() => {
        refresh();
      }, timeout);
    } catch {
      clearRefreshTimeout();
    }
  };

  /* 
  A useEffect hook that runs whenever the accessToken changes.
  It sets the Authorization header for axios requests and schedules a refresh
  timeout based on the token's expiration time. If the accessToken is null,
  it clears the Authorization header and any existing refresh timeout.

  @author IFD
  @date 2025-06-15
  */
  useEffect(() => {
    if (accessToken) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${accessToken}`;
      scheduleRefresh(accessToken);
    } else {
      delete axios.defaults.headers.common["Authorization"];
      clearRefreshTimeout();
    }
    return clearRefreshTimeout;
  }, [accessToken]);

  /* 
  A useEffect hook that runs once when the component mounts.
  It attempts to refresh the authentication state by calling the refresh function.
  If the refresh fails, it sets the user to null, isAuthenticated to false,
  and accessToken to null.

  @author IFD
  @date 2025-06-15
  */
  useEffect(() => {
    const tryRefresh = async () => {
      setGlobalLoading(true);
      const refreshed = await refresh();
      if (!refreshed) {
        setUser(null);
        setIsAuthenticated(false);
        setAccessToken(null);
      }
      setGlobalLoading(false);
    };
    tryRefresh();
  }, []);

  /* 
  A function to log in a user by sending a POST request
  to the login endpoint with the provided username and password.
  If the login is successful, it sets the user, accessToken, and isAuthenticated state.
  If the login fails, it sets the user to null, isAuthenticated to false,
  and accessToken to null, and throws the error.

  @param username - The username of the user trying to log in.
  @param password - The password of the user trying to log in.

  @author IFD
  @date 2025-06-15
  */
  const login = async (email: string, password: string) => {
    await axios
      .post(
        `${PM_API_ENDPOINTS.AUTH.LOGIN}`,
        {
          email,
          password,
        },
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        },
      )
      .then((response) => {
        const { user, accessToken } = response.data;
        setAccessToken(accessToken);
        setIsAuthenticated(true);
        setUser(user || {});
      })
      .catch((error) => {
        setUser(null);
        setIsAuthenticated(false);
        setAccessToken(null);
        throw error;
      });
  };

  /* 
  A function to register a new user by sending a POST request
  to the register endpoint with the provided username and password,
  and confirm password fields.

  @param username - The username of the user trying to register.
  @param password - The password of the user trying to register.
  @param confirmPassword - The confirmation password for the user trying to register.

  @author IFD
  @date 2025-06-30
  */
  const register = async (
    email: string,
    password: string,
    confirmPassword: string,
  ) => {
    await axios
      .post(
        `${PM_API_ENDPOINTS.AUTH.REGISTER}`,
        {
          email: email,
          password: password,
          confirmPassword: confirmPassword,
        },
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true,
        },
      )
      .then((response) => {
        const { user, accessToken } = response.data;
        setAccessToken(accessToken);
        setIsAuthenticated(true);
        setUser(user || {});
      })
      .catch((error) => {
        setUser(null);
        setIsAuthenticated(false);
        setAccessToken(null);
        throw error;
      });
  };

  /* 
  A function to refresh the access token by sending a POST request
  to the refresh endpoint. If the refresh is successful,
  it updates the accessToken and sets isAuthenticated to true.
  If the refresh fails, it sets the user to null, isAuthenticated to false,
  and accessToken to null, and returns false.
  
  @author IFD
  @date 2025-06-15
  */
  const refresh = async () => {
    setGlobalLoading(true);

    let refreshed = false;

    await axios
      .post(PM_API_ENDPOINTS.AUTH.REFRESH, {}, { withCredentials: true })
      .then((response) => {
        const { accessToken, user } = response.data;
        if (accessToken) {
          setAccessToken(accessToken);
          setIsAuthenticated(true);
          setUser(user || {});
          refreshed = true;
        }
      })
      .catch((error) => {
        console.error("Error refreshing access token:", error);
        setUser(null);
        setIsAuthenticated(false);
        setAccessToken(null);
        refreshed = false;
        throw error;
      })
      .finally(() => {
        setGlobalLoading(false);
      });

    return refreshed;
  };

  /* 
  A function to log out the user by sending a POST request
  to the logout endpoint. It clears the user, isAuthenticated,
  and accessToken state, effectively logging the user out.

  @author IFD
  @date 2025-06-15
  */
  const logout = async () => {
    setGlobalLoading(true);

    await axios
      .post(PM_API_ENDPOINTS.AUTH.LOGOUT, {}, { withCredentials: true })
      .catch((error) => {
        console.error("Error logging out:", error);
        throw error;
      })
      .finally(() => {
        setUser(null);
        setIsAuthenticated(false);
        setAccessToken(null);
        setGlobalLoading(false);
      });
  };

  /* 
  A context value that contains the user information,
  authentication status, access token, and functions 
  for login, logout, and refresh.

  @author IFD
  @date 2025-06-15
  */
  const value: AuthContextType = {
    user,
    isAuthenticated,
    accessToken,
    login,
    register,
    logout,
    refresh,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
