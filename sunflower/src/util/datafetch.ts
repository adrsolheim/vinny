import Tap from "../types/tap";

const BASE_URL = 'http://127.0.0.1:8080';

export async function fetchTaps() {
    const response = await fetch(`${BASE_URL}/api/taphouse`);
    const taps = (await response.json()) as Tap[];
    return taps;
};