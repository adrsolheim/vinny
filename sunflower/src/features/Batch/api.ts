import { Batch } from "./types";

const BASE_URL = 'http://127.0.0.1:8080';

export async function fetchBatches(): Promise<Batch[]> {
      const response = await fetch(`${BASE_URL}/api/batches`);
      const batches = (await response.json()) as Batch[];
      return batches;
};