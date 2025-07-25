export default interface AddressType {
  streetOne: string;
  streetTwo?: string;
  cityTown: string;
  stateProvince: string;
  postalZipCode: string;
  country: string;
  location?: any;
  unitSuite?: string;
}
