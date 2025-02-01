import logo from '../assets/orange_beer_mug.svg';
import styles from '../app.module.css'
import { useNavigate } from 'react-router-dom';

export default function Header({ sidebarActive, setSidebarActive } : { sidebarActive: boolean, setSidebarActive : Function}) {
  const activate = () => { setSidebarActive(true) };
  let navigate = useNavigate();
  const deactivate = () => { setSidebarActive(false) };//setTimeout(() => setSidebarActive(false), 500) };
  return (
    <header><div className={styles.headerlogo} onMouseLeave={deactivate}><img src={logo} alt="beer mug logo" onClick={() => navigate("/")} onMouseOver={activate}/></div></header>
  );
}