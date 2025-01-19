import { NavLink } from 'react-router-dom';
import styles from '../app.module.css';

export default function Sidebar({ active }: {active: boolean }) {
    const menuItems: string[] = ['Batches', 'Recipes', 'Profile'];
    return (
        <div className={ active ? `${styles.sidebar} ${styles.active}` : styles.sidebar}>
            <ul>
                {menuItems.map(item => <NavLink to={item.toLowerCase()}><li>{item}</li></NavLink>)}
            </ul>
        </div>
    );
}