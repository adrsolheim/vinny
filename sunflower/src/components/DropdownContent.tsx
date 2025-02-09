import styles from '../app.module.css';

export default function DropdownContent(props: DropdownContentProps) {
    const items = props.items;
    const handleClick = (i: string) => {
       props.setItem(i);
       props.setMenuOpen(false);
    };
    return (
        <div className={styles.dropdowncontent}>
            <ul>
                {items.map(i => <li onClick={() => handleClick(i)}>{i}</li>)}
            </ul>
        </div>
    );
}

interface DropdownContentProps {
    items: string[];
    setItem: Function;
    setMenuOpen: Function;
}