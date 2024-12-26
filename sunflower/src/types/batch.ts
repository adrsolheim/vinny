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
  interface Keg {
    id: number,
    capacity: number,
    brand: string,
    serialNumber: string,
    purchaseCondition: string,
    note: string   
  }