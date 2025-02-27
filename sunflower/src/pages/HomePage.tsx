import Tap from '../types/tap';
import { useState, useEffect } from 'react';
import TapCard from '../components/TapCard';
import { fetchTaps } from '../util/datafetch';


export default function HomePage() {
  const [taps, setTaps] = useState<Tap[]>(defaultTaps());

  useEffect(() => {
    const getTaps = async () => {
      const taps = await fetchTaps();
      setTaps(taps.toSorted((a,b) => a.id-b.id));
    };
    getTaps();
  }, []);
  
  return (
    <Taps taps={taps}/>
  );
}

function Taps({ taps } : { taps:  Tap[] }) {
  return (
    <main>
      {taps.map((t) => <TapCard key={t.id} tap={t}/>)}
    </main>

  );
}

function defaultTaps(): Tap[] {
  return Array(4).fill(null).map((_, i) => ({ id: i+1, active: false, batchUnit: undefined}))
}