import { Tap } from "./types";

const BASE_URL = 'http://127.0.0.1:8080';

export async function connectBatch(tapId: number, batchUnitId: number, oldBatchEmpty: boolean): Promise<Tap> {
    const response = await fetch(`${BASE_URL}/api/taphouse/${tapId}/connect/${batchUnitId}`, {
        method: 'POST',
        headers: generateHeaders(),
        body: JSON.stringify({ oldBatchEmpty: `${oldBatchEmpty}` })
    });
    const updatedTap = (await response.json()) as Tap;
    return updatedTap;
}

export async function fetchTaps(): Promise<Tap[]> {
    const response = await fetch(`${BASE_URL}/api/taphouse`, {method: 'GET', headers: generateHeaders()});
    const taps = (await response.json()) as Tap[];
    return taps;
};

export async function updateTap(tapId: number, batchUnitId: number): Promise<Tap> {
    const response = await fetch(`${BASE_URL}/api/taphouse/${tapId}/connect/${batchUnitId}`, {method: 'POST', headers: generateHeaders()});
    const tap = (await response.json()) as Tap;
    return tap;
}

function generateHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    };
}