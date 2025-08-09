import { useState } from "react";
import styles from '../../app.module.css';
import { Tap } from "../../features/Tap/types";
import DropdownButton from "../Button/DropdownButton";
import DropdownContent from "./DropdownContent";
import { BatchUnit } from "../../features/Batch/types";

export default function DropdownMenu(props: DropdownProps) {
    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    return(
        <div className={styles.dropdownMenu}>
            <DropdownButton menuOpen={menuOpen} setMenuOpen={setMenuOpen} buttonText={props.item.batchUnit?.name ?? '<empty>'}/>
            {menuOpen && <DropdownContent activeItem={props.item} items={props.items} setItem={props.setItem} setMenuOpen={setMenuOpen}/>}
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
