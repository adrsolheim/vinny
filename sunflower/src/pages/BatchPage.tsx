
import { useState } from "react";
import { useParams, useOutletContext } from "react-router-dom";
import Card from "../components/Card";

import Batch from "../types/batch";

export default function BatchPage() {
  const params = useParams<{batchId: string}>();
  //const [batches] = useState<Batch[]>([]);
  const batchesState = useOutletContext<Batch[]>();
  const bid: number = parseInt(params.batchId ?? "nan")
  const batch: Batch | undefined = isNaN(bid) ? undefined : batchesState.find(b => b.id === bid);
  if (batch === undefined) {
    return (
      <div>
        <p>Batch {bid} not found.</p>
      </div>
    )
  }
  return (
    <div>
        <p>{batch.id} - {batch.brewfatherId} - {batch.name}</p>
    </div>
  )
}