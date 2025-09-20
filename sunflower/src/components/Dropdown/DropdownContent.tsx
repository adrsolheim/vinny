import styles from '../../app.module.css';
import { BatchUnit } from '../../features/Batch/types';
import { connectBatch } from '../../features/Tap/api';
import { Tap } from '../../features/Tap/types';

export default function DropdownContent(props: DropdownContentProps) {
    const items = props.items;
    const updateTap = props.handleUpdateTap;
    const handleClick = (unit: BatchUnit) => {
        if (props.activeItem?.batchUnit && props.activeItem && props.activeItem.batchUnit.id !== unit.id) {
            connectBatch(props.activeItem.id, unit.id, true)
            .then((tap: Tap) => props.handleUpdateTap.apply(tap));
        }
        //props.activeItem.batchUnit = unit;
        //props.setItem(props.activeItem);
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
    activeItem: Tap;
    setItem: Function;
    setMenuOpen: Function;
    handleUpdateTap: Function;
}