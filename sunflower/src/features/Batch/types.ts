import { Recipe } from "../Recipe/types"

export interface Batch {
    id: number,
    brewfatherId: string,
    name: string,
    status: string,
    recipes: Recipe[],
    batchUnits: BatchUnit[]
}
export interface BatchUnit {
    id: number,
    batchId: number,
    name: string,
    brewfatherId: string,
    tapStatus: string,
    packaging: string,
    volumeStatus: string,
    keg: Keg
  }

export interface Keg {
    id: number,
    capacity: number,
    brand: string,
    serialNumber: string,
    purchaseCondition: string,
    note: string   
}
