import { useState, useEffect } from "react";
import { NavLink } from "react-router-dom";

import Batch from "../types/batch";

export default function BatchesPage() {
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