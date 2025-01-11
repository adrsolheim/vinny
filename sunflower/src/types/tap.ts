import Batch from "./batch";

export default interface Tap {
    id: number,
    active: boolean,
    batch: Batch | undefined
}