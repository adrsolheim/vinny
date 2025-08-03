import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";

import { Batch } from "../features/Batch/types";
import { fetchBatches } from "../features/Batch/api";
import BatchTable from "../features/Batch/components/BatchTable";

export default function BatchesPage() {
  const [batches, setBatches] = useState<Batch[]>([]);

  useEffect(() => {
    const fetchAllBatches = async () => {
      setBatches(await fetchBatches());
    };
    fetchAllBatches();
  }, []);


    return (
        <main>
            <BatchTable batches={batches} />            
            <Outlet context={batches}/>
        </main>
    );
}