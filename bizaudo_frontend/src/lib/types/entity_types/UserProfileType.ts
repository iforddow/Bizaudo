import type AddressType from "./AddressType";

export default interface UserProfileType {
  // User profile properties
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  profilePictureUrl?: string;
  address?: AddressType;
  profileType?: "tenant" | "propertyManager";
  profileSetup: boolean;
}
