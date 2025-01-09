import { NavLink } from 'react-router-dom';
import styles from '../app.module.css';

export default function HomePage() {
    return (
      <>
        <nav className={styles.nav}>
          <ul>
            <li>
              <NavLink to='/batches'>Batches</NavLink>
            </li>
            <li>
              <NavLink to='/recipes'>Recipes</NavLink>
            </li>
          </ul>
        </nav>
      </>
    );
}