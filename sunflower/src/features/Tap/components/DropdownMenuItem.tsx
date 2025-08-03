import { DropdownItemProps } from "./DropdownMenu";

export default function DropdownItem(props : Readonly<DropdownItemProps>) {
  return (
    <li>
      <a href="#">{props.icon} {props.operation}</a>
    </li>
  );
}