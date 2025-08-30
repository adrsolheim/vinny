import { Batch, BatchUnit } from "./types";

const BASE_URL = 'http://127.0.0.1:8080';

export async function fetchBatches(): Promise<Batch[]> {
      const response = await fetch(`${BASE_URL}/api/batches`);
      const batches = (await response.json()) as Batch[];
      return batches;
};

export async function fetchBatchUnits(): Promise<BatchUnit[]> {
    const response = await fetch(`${BASE_URL}/api/batches/units`);
    const batchUnits = (await response.json()) as BatchUnit[];
    return batchUnits;
}