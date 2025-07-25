const MIN_LENGTH = 8;
const MAX_LENGTH = 64;

export function validatePassword(
  password: string | null | undefined,
): string | null {
  if (!password || password.trim() === "") {
    return "Password cannot be empty";
  }

  if (password.length < MIN_LENGTH || password.length > MAX_LENGTH) {
    return `Password must be between ${MIN_LENGTH} and ${MAX_LENGTH} characters long`;
  }

  if (!/[A-Z]/.test(password)) {
    return "Password must contain at least one uppercase letter";
  }

  if (!/[a-z]/.test(password)) {
    return "Password must contain at least one lowercase letter";
  }

  if (!/\d/.test(password)) {
    return "Password must contain at least one digit";
  }

  if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
    return "Password must contain at least one special character";
  }

  return null;
}
