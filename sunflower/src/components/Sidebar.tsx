import { NavLink } from 'react-router-dom';
import styles from '../app.module.css';

export default function Sidebar({ active, setActive }: {active: boolean, setActive: Function }) {
    const menuItems: string[] = ['Batches', 'Recipes', 'Profile', 'Login'];
    const activate = () => setActive(true);
    const deactivate = () => setActive(false);
    return (
        <div className={ active ? `${styles.sidebar} ${styles.active}` : styles.sidebar} onMouseOver={activate} onMouseLeave={deactivate}>
            <ul>
                {menuItems.map((item, idx) => <NavLink key={idx} to={item.toLowerCase()}><li>{item}</li></NavLink>)}
            </ul>
        </div>
    );
}