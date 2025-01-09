import Tap from '../types/tap';
import { useState, useEffect } from 'react';
import Card from '../components/TapCard';

export default function HomePage() {
  const [taps, setTaps] = useState<Tap[]>([]);
  const BASE_URL = 'http://127.0.0.1:8080';

  useEffect(() => {
    const fetchTaps = async () => {
      const response = await fetch(`${BASE_URL}/api/taphouse`);
      const taps = (await response.json()) as Tap[];
      setTaps(taps);
    };
    fetchTaps();
  }, []);
    return (
        <div>
            {taps.sort((a,b) => a.id-b.id).map((t) => <Card tap={t}/>)}
        </div>
    );
}