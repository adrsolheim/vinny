import { Batch, BatchUnit } from "./types";

const BASE_URL = 'http://127.0.0.1:8080';

export async function fetchBatches(): Promise<Batch[]> {
      const response = await fetch(`${BASE_URL}/api/batches`, {method: 'GET', headers: generateHeaders()});
      const batches = (await response.json()) as Batch[];
      return batches;
};

export async function fetchBatchUnits(): Promise<BatchUnit[]> {
      const response = await fetch(`${BASE_URL}/api/batches/units`, {method: 'GET', headers: generateHeaders()});
      const batchUnits = (await response.json()) as BatchUnit[];
      return batchUnits;
}

export async function fetchAvailableBatchUnits(): Promise<BatchUnit[]> {
      const params = new URLSearchParams();
      params.set('volumeStatus', 'NOT_EMPTY');
      params.set('excludeTapStatus', 'CONNECTED');
      const response = await fetch(`${BASE_URL}/api/batches/units?${params}`, {method: 'GET', headers: generateHeaders()});
      const batchUnits = (await response.json()) as BatchUnit[];
      return batchUnits;
}

function generateHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    };
}