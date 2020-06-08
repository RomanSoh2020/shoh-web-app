import React, {useState} from 'react';
import './App.css';
import {Router, Switch, Route, Redirect } from "react-router-dom";
import { createBrowserHistory } from 'history';
import NavigationBar from './components/NavigationBar'
import SideBar from './components/SideBar'
import Home from "./components/Home";
import Login from "./components/Login";
import Utils from "./utils/Utils"
import {connect} from "react-redux";
import CookerListComponent from "./components/CookerListComponent";
import CookerComponent from "./components/CookerComponent";
import MealListComponent from "./components/MealListComponent";
import RestaurantListComponent from "./components/RestaurantListComponent";
import UserListComponent from "./components/UserListComponent";
import MyAccountComponent from "./components/MyAccountComponent";

const AuthRoute = props => {
    let user = Utils.getUser();
    if (!user) return <Redirect to="/login" />;
    return <Route {...props} />;
};

const history = createBrowserHistory();

// history - библиотечный класс который позволяет перемещаться (по страницам)
// по компонентам веб приложения. Например так history.push(“/home”).
// Router позволяет привязать компоненты к локальным путям внутри приложения и
// автоматизировать перемещение между компонентами с помощью history. Поэтому мы
// создаем и передаем объект history компоненту React через props.
function App(props) {
    const [exp, setExpanded] = useState(false);

    return (
        <div className="App">
            <Router history = { history }>
                <NavigationBar toggleSideBar={()=> setExpanded(!exp)}/>
                <div className="wrapper">
                    <SideBar expanded={exp}/>
                    <div className="container-fluid">
                        {props.error_message &&
                        <div className="alert alert-danger m-1">{props.error_message}</div>
                        }
                        <Switch>
                            <AuthRoute path="/home" component={Home}/>
                            <AuthRoute path="/cookers" exact component={CookerListComponent}/>
                            <AuthRoute path="/cookers/:id" component={CookerComponent}/>
                            <AuthRoute path="/meals" exact component={MealListComponent}/>
                            <AuthRoute path="/restaurants" exact component={RestaurantListComponent}/>
                            <AuthRoute path="/users" exact component={UserListComponent}/>
                            <AuthRoute path="/myaccount" exact component={MyAccountComponent}/>
                            <Route path="/login" component={Login} />
                        </Switch>
                    </div>
                </div>
            </Router>
        </div>
    );
}

function mapStateToProps(state) {
    const { msg } = state.alert;
    return { error_message: msg };
}

export default connect(mapStateToProps)(App);