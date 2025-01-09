import Batch from "./batch";

export default interface Tap {
    id: number,
    batch: Batch | undefined
}