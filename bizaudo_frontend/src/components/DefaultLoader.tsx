import React from "react";
import classes from "../css/DefaultLoader.module.css";

/* 
A default loader component that can be used throughout the application.
This component is a simple spinner that can be used to indicate loading states.
It is used in Mantine's theme UI, it intercepts the default loader and replaces 
it with this custom loader.

@author IFD
@since 2025-06-27
*/
const DefaultLoader = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(() => <span className={classes.loader}></span>);

export default DefaultLoader;
