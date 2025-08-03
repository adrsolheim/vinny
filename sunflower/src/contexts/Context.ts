import { createContext } from "react";
import Tap from "../features/Tap/types";

export const TapContext = createContext<Tap | null>(null);