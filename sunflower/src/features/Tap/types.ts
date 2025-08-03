import { BatchUnit } from "../Batch/types";

export interface Tap {
    id: number,
    active: boolean,
    batchUnit: BatchUnit | undefined
}

export interface ModalProps {
  open: boolean;
  setOpen: Function;
  batchUnits: BatchUnit[];
}