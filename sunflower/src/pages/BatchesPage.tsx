import { useState, useEffect } from "react";
import { NavLink, Outlet } from "react-router-dom";

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
            <BatchTable batches={batches} />            
            <div>
              <Outlet context={batches}/>
            </div>
        </div>
    );
}

function BatchTable({batches}: { batches: Batch[]}) {
  return (
    <>
    <tr>
      <th>Batches</th>
    </tr>
   { batches.map(b => <BatchRow batch={b}/>) }
    </>
  );
}

function BatchRow({ batch}: {batch: Batch}) {
  return (
    <tr>
      <td>{batch.id}</td>
      <td>{batch.name}</td>
      <td>{batch.status}</td>
    </tr>

  );
}