import { createContext, useContext } from 'react';
export const LayoutCtx = createContext({ abrirConversas: () => { } });
export const useLayout = () => useContext(LayoutCtx);
