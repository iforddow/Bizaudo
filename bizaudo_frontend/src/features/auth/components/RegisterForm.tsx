import { useState } from "react";
import useAuth from "../../../hooks/AuthenticationHook";
import { useForm } from "@mantine/form";
import { showErrorNotification } from "../../../lib/utils/NotificationManager";
import { ActionIcon, Button, Space, TextInput } from "@mantine/core";
import { IconEye, IconEyeOff } from "@tabler/icons-react";
import { validatePassword } from "../../../lib/utils/PasswordValidator";
import { useNavigate } from "react-router-dom";

export default function RegisterForm({
  startLoading,
  stopLoading,
}: {
  startLoading: () => void;
  stopLoading: () => void;
}) {
  // Use the custom authentication context to access login functionality
  const { register } = useAuth();

  // Use the useNavigate hook from react-router-dom for navigation
  const navigate = useNavigate();

  //Effect to show or hide the password
  const [showPassword, setShowPassword] = useState(false);
  const [passwordError, setPasswordError] = useState<string | null>(null);

  //Effect to show or hide the confirm password password
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [confirmPasswordError, setConfirmPasswordError] = useState<
    string | null
  >(null);

  // Manage password in local state only
  const [password, setPassword] = useState("");

  // Manage confirm password in local state only
  const [confirmPassword, setConfirmPassword] = useState("");

  /* 
  A form for user registration. It uses the Mantine library's useForm hook
  to manage form state and validation. The form includes an email field
  with validation for a valid email format. The password and confirm password fields
  are managed in local state, and the form validates that the password and confirm password
  match before submission.

  @author IFD
  @since 2025-06-29
  */
  const registerForm = useForm({
    mode: "uncontrolled",
    initialValues: {
      email: "",
    },

    validate: {
      email: (value) => {
        if (!value) {
          return "Email is required";
        } else if (!/^\S+@\S+$/.test(value)) {
          return "Invalid email format";
        } else {
          return null;
        }
      },
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
  const handleRegister = async (values: { email: string }) => {
    // Validate password before submit
    if (validatePassword(password) !== null) {
      setPasswordError(validatePassword(password));
      return;
    }

    if (!confirmPassword || confirmPassword.trim() === "") {
      setConfirmPasswordError("Confirm Password cannot be empty");
      return;
    } else if (password !== confirmPassword) {
      setConfirmPasswordError("Passwords do not match");
      return;
    }

    setPasswordError(null);
    setConfirmPasswordError(null);

    startLoading();

    await register(values.email, password, confirmPassword)
      .then(() => {
        navigate("/", { replace: true });

        /* 
        Note:

        This eventully should be some sort of onboarding process
        or redirect to a welcome page. For now, we just redirect 
        to the home page.
        
        */
      })
      .catch((error) => {
        const status = error?.response?.status;

        let message = "Registration failed. Please try again.";
        if (status === 409) {
          message = "User already exists.";
          registerForm.setErrors({ email: message });
        } else {
          showErrorNotification(
            "Registration Error",
            "There was a problem trying to register your account.",
          );
        }
      })
      .finally(() => {
        stopLoading();
      });
  };

  return (
    <form
      onSubmit={registerForm.onSubmit(async (values) => {
        await handleRegister(values);
      })}
    >
      <TextInput
        label="Email"
        placeholder=""
        key={registerForm.key("email")}
        {...registerForm.getInputProps("email")}
        required
      />
      <Space h="md" />
      <TextInput
        label="Password"
        placeholder=""
        key={registerForm.key("password")}
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
      <Space h="md" />
      <TextInput
        label="Confirm Password"
        placeholder=""
        key={registerForm.key("confirmPassword")}
        onChange={(e) => setConfirmPassword(e.currentTarget.value)}
        error={confirmPasswordError}
        autoComplete="off"
        type={showConfirmPassword ? "text" : "password"}
        rightSection={
          <ActionIcon
            c={"dimmed"}
            variant="transparent"
            style={{ marginInlineEnd: "var(--input-padding-inline-start)" }}
          >
            {showConfirmPassword ? (
              <IconEyeOff onClick={() => setShowConfirmPassword(false)} />
            ) : (
              <IconEye onClick={() => setShowConfirmPassword(true)} />
            )}
          </ActionIcon>
        }
        required
      />
      <Space h="md" />
      <Button w={"100%"} mt={"md"} type="submit">
        Sign up
      </Button>
    </form>
  );
}
