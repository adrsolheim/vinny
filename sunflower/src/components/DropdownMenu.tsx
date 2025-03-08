import { useState } from "react";
import DropdownButton from "./DropdownButton";
import DropdownContent from "./DropdownContent";
import styles from '../app.module.css';
import BatchUnit from "../types/batchUnit";
import Tap from "../types/tap";

export default function DropdownMenu(props: DropdownProps) {
    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    return(
        <div className={styles.dropdownMenu}>
            <DropdownButton menuOpen={menuOpen} setMenuOpen={setMenuOpen} buttonText={props.item.batchUnit?.name ?? '<empty>'}/>
            {menuOpen && <DropdownContent items={props.items} setItem={props.setItem} setMenuOpen={setMenuOpen}/>}
        </div>
    );
}


interface DropdownProps {
    menuOpen: boolean;
    setMenuOpen: Function;
    item: Tap;
    items: BatchUnit[];
    setItem: Function;
}
