
import { useState } from "react";
import { useParams } from "react-router-dom";

import Batch from "../types/batch";

export default function BatchPage() {
  const params = useParams<{batchId: string}>();
  const [batches] = useState<Batch[]>([]);
  const bid: number = parseInt(params.batchId ?? "nan")
  const batch: Batch | undefined = isNaN(bid) ? undefined : batches.find(b => b.id == bid);
  if (batch === undefined) {
    return (
      <div>
        <p>Batch not found</p>
      </div>
    )
  }
  return (
    <div>
        <p>{batch.id} - {batch.brewfatherId} - {batch.name}</p>
    </div>
  )
}