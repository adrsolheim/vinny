import BatchRow from "./BatchRow";

export default function BatchTable({batches}: { batches: Batch[]}) {
  return (
    <table>
      <thead>
        <tr>
          <th>Batches</th>
        </tr>
      </thead>
      <tbody>
        { batches.map(b => <BatchRow key={b.id} batch={b}/>) }
      </tbody>
    </table>
  );
}