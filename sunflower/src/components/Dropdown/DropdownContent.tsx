import styles from '../../app.module.css';
import { BatchUnit } from '../../features/Batch/types';

export default function DropdownContent(props: DropdownContentProps) {
    const items = props.items;
    const handleClick = (unit: BatchUnit) => {
       props.setItem(unit.name);
       props.setMenuOpen(false);
    };
    return (
        <div className={styles.dropdowncontent}>
            <ul>
                {items.map((bu, i) => <li key={i} onClick={() => handleClick(bu)}>{bu.name}</li>)}
            </ul>
        </div>
    );
}

interface DropdownContentProps {
    items: BatchUnit[];
    setItem: Function;
    setMenuOpen: Function;
}