import logo from '../assets/orange_beer_mug.svg';
import styles from '../app.module.css'

export default function Header({ sidebarActive, setSidebarActive } : { sidebarActive: boolean, setSidebarActive : Function}) {
  const clickToggle = () => { setSidebarActive(!sidebarActive) };
  const activate = () => { setSidebarActive(true) };
  const deactivate = () => { setSidebarActive(false) };//setTimeout(() => setSidebarActive(false), 500) };
  return (
    <header><div className={styles.headerlogo} onMouseLeave={deactivate}><img src={logo} alt="beer mug logo" onClick={clickToggle} onMouseOver={activate}/></div></header>
  );
}