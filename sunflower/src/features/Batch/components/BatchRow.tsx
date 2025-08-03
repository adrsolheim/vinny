import { Batch } from "../types";

export default function BatchRow({ batch}: {batch: Batch}) {
  return (
    <tr>
      <td>{batch.id}</td>
      <td>{batch.name}</td>
      <td>{batch.status}</td>
    </tr>

  );
}