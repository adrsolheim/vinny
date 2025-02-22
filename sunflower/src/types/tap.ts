import BatchUnit from "./batchUnit";

export default interface Tap {
    id: number,
    active: boolean,
    batchUnit: BatchUnit | undefined
}