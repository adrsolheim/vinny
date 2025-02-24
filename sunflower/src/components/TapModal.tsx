import { createPortal } from "react-dom";
import styles from '../app.module.css';
import { BaselineAddCircleOutline } from '../assets/BaselineAddCircleOutline';
import DropdownMenu from "./DropdownMenu";
import { useEffect, useState } from "react";
import BatchUnit from "../types/batchUnit";
import { fetchBatchUnits } from "../util/datafetch";
import StandardButton from "./StandardButton";

export default function TapModal(props: Readonly<ModalProps>) {
    const open = props.open;
    const batchUnits = props.batchUnits;
    return (
        <>       
        {open && createPortal(
            <ModalContent open={props.open} setOpen={props.setOpen} batchUnits={batchUnits}/>,
            document.body
        )}
        </>   
    );
}

function ModalContent (props: Readonly<ModalProps>) {
    const batchUnits = props.batchUnits;
    const [activeItem, setActiveItem] = useState<string>('...');
    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    const close = () => {
      props.setOpen(false); 
    };
    return (
        <div className={styles.modal}>
          <div className={styles.modalcontent}>
            <DropdownMenu item={activeItem} items={batchUnits} menuOpen={menuOpen} setMenuOpen={setMenuOpen} setItem={setActiveItem}/>
            <StandardButton onClick={() => props.setOpen(false)} text="Close" end={true}/>
          </div>
        </div>
    );
}

interface ModalProps {
  open: boolean;
  setOpen: Function;
  batchUnits: BatchUnit[];
}

interface CardButtonProps {
  // this should be React.ReactElement?
  icon: JSX.Element;
  open: boolean;
  setOpen: Function;
  children?: React.ReactNode;
  end?: boolean;
}
function CardButton(props : Readonly<CardButtonProps>) {
  const setOpen = props.setOpen;
  return (
    <li className={`${styles.cardbutton} ${props.end ? styles.last : ''}`}>
      <a className={styles.iconbutton} href="#" onClick={() => setOpen(false)}>
        {props.icon}
      </a>
    </li>
  );
}