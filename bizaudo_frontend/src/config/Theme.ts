import { createTheme, Loader } from "@mantine/core";
import DefaultLoader from "../components/DefaultLoader";

/* 
A Crimson theme for the application.

@author: IFD
*/
export const defaultTheme = createTheme({
  //! APP COLORS

  // Primary color
  primaryColor: "primary",

  // Color scheme
  colors: {
    primary: [
      "#edf2fd",
      "#d8e0f4",
      "#acbfeb",
      "#7e9be4",
      "#587ddd",
      "#416ada",
      "#3560da",
      "#2950c1",
      "#2247ad",
      "#153e9b",
    ],
  },

  // Default black color
  black: "#0B1215",

  // Default white color
  white: "#F8F8FF",

  //! COLOR SETTINGS

  // The primary color shade to use
  primaryShade: 5,

  // Disable auto contrast for all components
  autoContrast: true,

  // The threshold to use for luminance-based color scheme switching
  // will switch text color to black if the luminance of the background
  // color is above this value
  luminanceThreshold: 0.5,

  //! APP BREKPOINTS

  // App breakpoints
  breakpoints: {
    xs: "36em",
    sm: "48em",
    md: "62em",
    lg: "75em",
    xl: "88em",
  },

  //! APP LOOK AND FEEL

  // Shows a focus ring on elements when they are focused
  focusRing: "auto",

  // Default radius for components
  defaultRadius: "md",

  // Radius sizes
  radius: {
    xs: "0.125rem",
    sm: "0.25rem",
    md: "0.375rem",
    lg: "0.5rem",
    xl: "0.75rem",
    xxl: "1rem",
  },

  defaultGradient: {
    from: "primary.4",
    to: "primary.8",
    deg: 45,
  },

  // Cursor style for all components
  cursorType: "pointer",

  // Shadow style for all components
  shadows: {
    xs: "0 1px 2px rgba(0, 0, 0, 0.05)",
    sm: "0 1px 3px rgba(0, 0, 0, 0.1)",
    md: "0 1px 5px rgba(0, 0, 0, 0.15)",
    lg: "0 1px 10px rgba(0, 0, 0, 0.2)",
    xl: "0 1px 20px rgba(0, 0, 0, 0.25)",
  },

  // Scale factor for all components
  scale: 1,

  //! COMPONENT SETTINGS

  components: {
    // Override loader styles
    Loader: Loader.extend({
      defaultProps: {
        loaders: { ...Loader.defaultLoaders, custom: DefaultLoader },
        type: "custom",
      },
    }),
  },

  //! FONT SETTINGS

  // Enable font smoothing
  fontSmoothing: true,

  // Font family for all components
  fontFamily:
    "Roboto, -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif",

  // Font family for headings
  headings: {
    fontFamily:
      "Poppins, -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial, sans-serif",
  },
});
