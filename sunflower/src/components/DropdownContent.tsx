import styles from '../app.module.css';

export default function DropdownContent(props: DropdownContentProps) {
    const items = props.items;
    return (
        <div className={styles.dropmenucontent}>
            <ul>
                {items.map(i => <li>{i}</li>)}
            </ul>
        </div>
    );
}

interface DropdownContentProps {
    items: string[];
}