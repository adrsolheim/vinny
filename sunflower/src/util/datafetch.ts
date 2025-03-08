import BatchUnit from "../types/batchUnit";
import Tap from "../types/tap";

const BASE_URL = 'http://127.0.0.1:8080';

export async function fetchTaps(): Promise<Tap[]> {
    const response = await fetch(`${BASE_URL}/api/taphouse`);
    const taps = (await response.json()) as Tap[];
    return taps;
};

export async function fetchBatchUnits(): Promise<BatchUnit[]> {
    const response = await fetch(`${BASE_URL}/api/batches/units`);
    const batchUnits = (await response.json()) as BatchUnit[];
    return batchUnits;
}

export async function updateTap(tapId: number, batchUnitId: number): Promise<Tap> {
    const response = await fetch(`${BASE_URL}/api/taphouse/${tapId}/connect/${batchUnitId}`);
    const tap = (await response.json()) as Tap;
    return tap;
}

