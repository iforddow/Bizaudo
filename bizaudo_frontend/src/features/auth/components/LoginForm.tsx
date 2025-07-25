import { ActionIcon, Anchor, Button, Space, TextInput } from "@mantine/core";
import { useForm } from "@mantine/form";
import { IconEye, IconEyeOff } from "@tabler/icons-react";
import { useState } from "react";
import useAuth from "../../../hooks/AuthenticationHook";
import { showErrorNotification } from "../../../lib/utils/NotificationManager";
import { useNavigate } from "react-router-dom";

export default function LoginForm({
  startLoading,
  stopLoading,
}: {
  startLoading: () => void;
  stopLoading: () => void;
}) {
  // Use the custom authentication context to access login functionality
  const { login } = useAuth();

  // Use the useNavigate hook from react-router-dom for navigation
  const navigate = useNavigate();

  //Effect to show or hide the password
  const [showPassword, setShowPassword] = useState(false);
  const [passwordError, setPasswordError] = useState<string | null>(null);

  // Manage password in local state only
  const [password, setPassword] = useState("");

  /* 
  A form for user login. It uses the Mantine library's useForm hook
  to manage form state and validation. The form includes an email field
  with validation for a valid email format.

  @author IFD
  @since 2025-06-29
  */
  const loginForm = useForm({
    mode: "uncontrolled",
    initialValues: {
      email: "",
    },

    validate: {
      email: (value) => (/^\S+@\S+$/.test(value) ? null : "Invalid email"),
    },
  });

  /* 
  A function to handle the login process.
  It opens a loading state, attempts to log in with the provided
  email and password, and handles errors based on the response status.
  If the login is successful, it will proceed without further action.

  @param {Object} values - The form values containing email and password.

  @author IFD
  @date 2025-06-15
  */
  const handleLogin = async (values: { email: string }) => {
    // Validate password before submit
    if (!password) {
      setPasswordError("Password cannot be empty");
      return;
    }
    setPasswordError(null);

    startLoading();

    await login(values.email, password)
      .then(() => {
        navigate("/", { replace: true });

        // Login successful, you can redirect or show a success message
      })
      .catch((error) => {
        const status = error?.response?.status;

        let message = "Login failed. Please try again.";
        if (status === 404) {
          message = "User not found.";
          loginForm.setErrors({ email: message });
        } else if (status === 401) {
          message = "Incorrect password.";
          setPasswordError(message);
        } else {
          showErrorNotification(
            "Login Error",
            "There was a problem trying to sign you in.",
          );
        }
      })
      .finally(() => {
        stopLoading();
      });
  };

  return (
    <form
      onSubmit={loginForm.onSubmit(async (values) => {
        await handleLogin(values);
      })}
    >
      <TextInput
        label="Email"
        placeholder=""
        key={loginForm.key("email")}
        {...loginForm.getInputProps("email")}
        required
      />
      <Space h="md" />
      <TextInput
        label="Password"
        placeholder=""
        key={loginForm.key("password")}
        onChange={(e) => setPassword(e.currentTarget.value)}
        error={passwordError}
        autoComplete="off"
        type={showPassword ? "text" : "password"}
        rightSection={
          <ActionIcon
            c={"dimmed"}
            variant="transparent"
            style={{ marginInlineEnd: "var(--input-padding-inline-start)" }}
          >
            {showPassword ? (
              <IconEyeOff onClick={() => setShowPassword(false)} />
            ) : (
              <IconEye onClick={() => setShowPassword(true)} />
            )}
          </ActionIcon>
        }
        required
      />
      <Anchor
        href={"/forgot-password"}
        underline="hover"
        size="xs"
        c={"dimmed"}
      >
        Forgot Password?
      </Anchor>
      <Button w={"100%"} mt={"md"} type="submit">
        Log in
      </Button>
    </form>
  );
}
