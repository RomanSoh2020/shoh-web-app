import React from 'react';
import { Link } from 'react-router-dom'
import { Nav } from 'react-bootstrap'
import {
    faStore,
    faUser,
    faUsers
} from "@fortawesome/fontawesome-free-solid";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHamburger, faMitten} from "@fortawesome/free-solid-svg-icons";


class SideBar extends React.Component {

    constructor(props) {
        super(props);
    }

    // Вариант прорисовки выбирается с помощью свойства expanded, которое может принимать значение
    // true или false.
    render() {
        return (
            <>
                { this.props.expanded &&
                <Nav className={"flex-column my-sidebar my-sidebar-expanded"}>
                    <Nav.Item><Nav.Link as={Link} to="/cookers"><FontAwesomeIcon icon={faMitten}/>{' '}Повары</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/meals"><FontAwesomeIcon icon={faHamburger}/>{' '}Блюда</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/restaurants"><FontAwesomeIcon icon={faStore}/>{' '}Рестораны</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/users"><FontAwesomeIcon icon={faUsers}/>{' '}Пользователи</Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/myaccount"><FontAwesomeIcon icon={faUser}/>{' '}Мой аккаунт</Nav.Link></Nav.Item>
                </Nav>
                }
                { !this.props.expanded &&
                <Nav className={"flex-column my-sidebar my-sidebar-collapsed"}>
                    <Nav.Item><Nav.Link as={Link} to="/cookers"><FontAwesomeIcon icon={faMitten} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/meals"><FontAwesomeIcon icon={faHamburger} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/restaurants"><FontAwesomeIcon icon={faStore} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/users"><FontAwesomeIcon icon={faUsers} size="2x"/></Nav.Link></Nav.Item>
                    <Nav.Item><Nav.Link as={Link} to="/myaccount"><FontAwesomeIcon icon={faUser} size="2x"/></Nav.Link></Nav.Item>
                </Nav>
                }
            </>
        );
    }
}

export default SideBar;