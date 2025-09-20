import { Tap } from '../features/Tap/types';
import { useState, useEffect } from 'react';
import TapCard from '../features/Tap/components/TapCard';
import { fetchTaps } from '../features/Tap/api';


export default function TapsPage() {
  const [taps, setTaps] = useState<Tap[]>(defaultTaps());
  const handleUpdateTap = (tap: Tap) => {
    console.log('Updating tap in TapsPage', tap);
    setTaps(taps.map(t => t.id === tap.id ? tap : t));
  }

  useEffect(() => {
    const getTaps = async () => {
      const taps = await fetchTaps();
      setTaps(taps.toSorted((a,b) => a.id-b.id));
    };
    getTaps();
  }, []);
  
  return (
    <main>
      {taps.map((t) => <TapCard key={t.id} tap={t} handleUpdateTap={handleUpdateTap}/>)}
    </main>
  );
}

function defaultTaps(): Tap[] {
  return Array(4).fill(null).map((_, i) => ({ id: i+1, active: false, batchUnit: undefined}))
}