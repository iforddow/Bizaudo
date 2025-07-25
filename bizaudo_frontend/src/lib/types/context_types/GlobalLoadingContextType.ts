/* 
A TypeScript interface for the loading context.

@author IFD
@date 2025-06-15
*/
export interface GlobalLoadingContextType {
  globalLoading: boolean;
  setGlobalLoading: (state: boolean) => void;
}
