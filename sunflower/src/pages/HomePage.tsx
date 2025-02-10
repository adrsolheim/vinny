import Tap from '../types/tap';
import { useState, useEffect } from 'react';
import Card from '../components/TapCard';

export default function HomePage() {
  const [taps, setTaps] = useState<Tap[]>(defaultTaps());
  const BASE_URL = 'http://127.0.0.1:8080';

  useEffect(() => {
    const fetchTaps = async () => {
      const response = await fetch(`${BASE_URL}/api/taphouse`);
      const taps = (await response.json()) as Tap[];
      setTaps(taps.sort((a,b) => a.id-b.id));
    };
    fetchTaps();
  }, []);
  
  return (
    <Taps taps={taps}/>
  );
}

function Taps({ taps } : { taps:  Tap[] }) {
  return (
    <main>
      {taps.map((t) => <Card key={t.id} tap={t}/>)}
    </main>

  );
}

function defaultTaps(): Tap[] {
  return Array(4).fill(null).map((_, i) => ({ id: i+1, active: false, batch: undefined}))
}