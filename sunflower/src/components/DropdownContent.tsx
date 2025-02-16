import styles from '../app.module.css';
import BatchUnit from '../types/batchUnit';

export default function DropdownContent(props: DropdownContentProps) {
    const items = props.items;
    const handleClick = (unit: BatchUnit) => {
       props.setItem(unit.name);
       props.setMenuOpen(false);
    };
    return (
        <div className={styles.dropdowncontent}>
            <ul>
                {items.map(i => <li onClick={() => handleClick(i)}>{i.name}</li>)}
            </ul>
        </div>
    );
}

interface DropdownContentProps {
    items: BatchUnit[];
    setItem: Function;
    setMenuOpen: Function;
}