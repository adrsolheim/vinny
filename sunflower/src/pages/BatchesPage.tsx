import { useState, useEffect } from "react";
import { NavLink } from "react-router-dom";
export default function BatchesPage() {
  interface Batch {
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
  interface Recipe {
    id: number,
    brewfatherId: string,
    name: string
  }
  const [batches, setBatches] = useState<Batch[]>([]);
  const BASE_URL = 'http://127.0.0.1:8080';

  useEffect(() => {
    const fetchBatches = async () => {
      const response = await fetch(`${BASE_URL}/api/batches`);
      const batches = (await response.json()) as Batch[];
      setBatches(batches);
    };
    fetchBatches();
  }, []);

    return (
        <div>
            <NavLink to='/'>Back</NavLink>
            <h1>Brews</h1>
            <div className="card">
                <ul>
                {batches.map((batch) => {
                    return <li key={batch.id}>{batch.name}</li>
                })}
                </ul>
            </div>
        </div>
    );
}