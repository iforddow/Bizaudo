import type AddressType from "./AddressType";

/* 
A TypeScript interface representing a user in the application.
This interface defines the structure of a user object, which
includes an ID and an email address.

@author IFD
@since 2025-06-27
*/
export interface UserType {
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

  // User profile properties
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  profilePictureUrl?: string;
  address?: AddressType;
  profileType?: "tenant" | "propertyManager";
  profileSetup: boolean;
}
