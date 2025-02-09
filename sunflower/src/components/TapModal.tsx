import { createPortal } from "react-dom";
import styles from '../app.module.css';
import { BaselineAddCircleOutline } from '../assets/BaselineAddCircleOutline';
import DropdownMenu from "./DropdownMenu";
import { useState } from "react";

export default function TapModal(props: Readonly<ModalProps>) {
    const open = props.open;
    return (
        <>       
        {open && createPortal(
            <ModalContent open={props.open} setOpen={props.setOpen}/>,
            document.body
        )}
        </>   
    );
}

function ModalContent (props: Readonly<ModalProps>) {
    const itemList: string [] = ['hello', 'foo', 'bar'];
    const [menuOpen, setMenuOpen] = useState<boolean>(false);
    return (
        <div className={styles.modal}>
          <div className={styles.modalcontent}>
            <DropdownMenu item={itemList[0]} items={itemList} menuOpen={menuOpen} setMenuOpen={setMenuOpen}/>
            <CardButton icon={<BaselineAddCircleOutline color='white' />} open={props.open} setOpen={props.setOpen} />
          </div>
        </div>
    );
}

interface ModalProps {
  open: boolean;
  setOpen: Function;
}

interface CardButtonProps {
  // this should be React.ReactElement?
  icon: JSX.Element;
  open: boolean;
  setOpen: Function;
  children?: React.ReactNode;
}
function CardButton(props : Readonly<CardButtonProps>) {
  const setOpen = props.setOpen;
  return (
    <li className={styles.cardbutton}>
      <a className={styles.iconbutton} href="#" onClick={() => setOpen(false)}>
        {props.icon}
      </a>
    </li>
  );
}