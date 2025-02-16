import Keg from "./keg";

export default interface BatchUnit {
    id: number,
    batchId: number,
    brewfatherId: string,
    name: string,
    tapStatus: string,
    packaging: string,
    volumeStatus: string,
    keg: Keg
}