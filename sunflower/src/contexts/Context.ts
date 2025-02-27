import { createContext } from "react";
import Tap from "../types/tap";

export const TapContext = createContext<Tap | null>(null);