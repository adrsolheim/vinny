import Keg from "./keg"
import { Recipe } from "./recipe"

export default interface Batch {
    id: number,
    brewfatherId: string,
    name: string,
    status: string,
    recipes: Recipe[],
    batchUnits: BatchUnit[]
}
  interface BatchUnit {
    id: number,
    batchId: number,
    tapStatus: string,
    packaging: string,
    volumeStatus: string,
    keg: Keg
  }