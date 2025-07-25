/* 
A function to shorten an email address to the 
first two characters in uppercase.

@author IFD
@date 2025-07-01
*/
export const shortenEmail = (email?: string): string => {
  // Check if the email is undefined or null or has less than 2 characters
  // If so, return "N/A"
  if (!email || email.length < 2) {
    return "N/A";
  }

  // Extract the first two characters of the email and convert them to uppercase
  const shortenedEmail = email.substring(0, 2).toLocaleUpperCase();

  // Return the shortened email
  return shortenedEmail;
};
