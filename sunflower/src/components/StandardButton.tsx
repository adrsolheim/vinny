import styles from '../app.module.css';

export default function StandardButton(props : StandardButtonProps) {
    return <button className={`${styles.standardbutton} ${props.end ? styles.last : ''}`} onClick={props.onClick}>
        {props.text}
    </button>
}

interface StandardButtonProps {
    end?: boolean;
    text: string;
    onClick: React.MouseEventHandler<HTMLButtonElement>;
}