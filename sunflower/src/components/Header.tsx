import logo from '../assets/orange_beer_mug.svg';

export default function Header({ sidebarActive, setSidebarActive } : { sidebarActive: boolean, setSidebarActive : Function}) {
  const clickToggle = () => { setSidebarActive(!sidebarActive) };
  return (
    <header><img src={logo} alt="beer mug logo" onClick={clickToggle}/></header>
  );
}