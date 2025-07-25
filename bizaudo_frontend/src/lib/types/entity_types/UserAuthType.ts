export default interface UserAuthType {
  id: string;
  email: string;

  // User auth entity properties
  createdAt: string;
  expired: boolean;
  locked: boolean;
  credentialsExpired: boolean;
  enabled: boolean;
  lastActive: string;
  emailConfirmed: boolean;
}
