import Tap from '../types/tap';
import { useState, useEffect } from 'react';
import Card from '../components/TapCard';
import { NavLink } from 'react-router-dom';
import styles from '../app.module.css';
import logo from '../assets/orange_beer_mug.svg';

export default function HomePage() {
  const [taps, setTaps] = useState<Tap[]>([]);
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
      <>
        <Header />
        <Taps taps={taps}/>
      </>
    );
}

function Header() {
  const navList: string[] = ['Batches', 'Recipes'];
  return <header className={styles.navbar}>
            <NavLink to={'/'}><img src={logo} alt='beer mug logo' style={{ height: '50px' }} /></NavLink>
            <nav className={styles.navlist}>
              <ul>
                {navList.map((path, idx) => <li><NavLink key={idx} to={path.toLowerCase()}>{path}</NavLink></li>)}
              </ul>
            </nav>
          </header>
}

function Taps({ taps } : { taps:  Tap[] }) {
  return (
    <div>
      {taps.map((t) => <Card tap={t}/>)}
    </div>

  );
}