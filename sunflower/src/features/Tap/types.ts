import { BatchUnit } from "../Batch/types";

export interface Tap {
    id: number,
    active: boolean,
    batchUnit: BatchUnit | undefined
}