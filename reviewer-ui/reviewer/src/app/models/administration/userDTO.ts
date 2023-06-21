import { ElementPosition } from "../shared/element-position";

export class UserDTO extends ElementPosition {
    username: string = "";
    email: string = "";
    isActivated: boolean = false;
    isDisabled: boolean = false;
    authorities: string[] = [];
}