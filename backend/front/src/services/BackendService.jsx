// Реализация интерфейса с REST сервисом на стороне клиента

import axios from 'axios'
import Utils from '../utils/Utils'
import {alertActions, store} from "../utils/Rdx";

const API_URL = 'http://localhost:8081/api/v1';
const AUTH_URL = 'http://localhost:8081/auth';

class BackendService {

    /* Auth */

    login(login, password) {
        return axios.post(`${AUTH_URL}/login`, {login, password})
    }

    logout() {
        /* return axios.get (`${AUTH_URL}/logout`, { headers: {Authorization: Utils.getToken() }}) */
        return axios.get (`${AUTH_URL}/logout` )
    }

    /* Cookers */
    /* Методы для работы с бэкэндовским CookerController */

    retrieveAllCookers(page, limit) {
        return axios.get(`${API_URL}/cookers?page=${page}&limit=${limit}`);
    }

    retrieveCooker(id) {
        return axios.get(`${API_URL}/cookers/${id}`);
    }

    createCooker(cooker) {
        return axios.post(`${API_URL}/cookers`, cooker);
    }

    updateCooker(cooker) {
        return axios.put(`${API_URL}/cookers/${cooker.id}`, cooker);
    }

    deleteCookers(cookers) {
        return axios.post(`${API_URL}/deletecookers`, cookers);
    }

    /* Restaurants */

    retrieveAllRestaurants(page, limit) {
        return axios.get(`${API_URL}/restaurants?page=${page}&limit=${limit}`);
    }

    retrieveRestaurant(id) {
        return axios.get(`${API_URL}/restaurants/${id}`);
    }

    createRestaurant(restaurants) {
        return axios.post(`${API_URL}/restaurants`, restaurants);
    }

    updateRestaurant(restaurants) {
        return axios.put(`${API_URL}/restaurant/${restaurants.id}`, restaurants);
    }

    deleteRestaurants(restaurants) {
        return axios.post(`${API_URL}/deleterestaurants`, restaurants);
    }

    /* Meals */

    retrieveAllMeals(page, limit) {
        return axios.get(`${API_URL}/meals?page=${page}&limit=${limit}`);
    }

    retrieveMeal(id) {
        return axios.get(`${API_URL}/meals/${id}`);
    }

    createMeal(song) {
        return axios.post(`${API_URL}/meals`, meal);
    }

    updateMeal(meal) {
        return axios.put(`${API_URL}/meals/${meal.id}`, meal);
    }

    deleteMeals(meals) {
        return axios.post(`${API_URL}/deletemeals`, meals);
    }

    /* Users */

    retrieveAllUsers(page, limit) {
        return axios.get(`${API_URL}/users?page=${page}&limit=${limit}`);
    }

    retrieveUser(id) {
        return axios.get(`${API_URL}/users/${id}`);
    }

    createUsers(user) {
        return axios.post(`${API_URL}/users`, user);
    }

    updateUser(user) {
        return axios.put(`${API_URL}/users/${user.id}`, user);
    }

    deleteUsers(users) {
        return axios.post(`${API_URL}/deleteusers`, users);
    }

}

function showError(msg)
{
    store.dispatch(alertActions.error(msg));
}

axios.interceptors.request.use(
    config => {
        store.dispatch(alertActions.clear());
        let token = Utils.getToken();
        if (token)
            config.headers.Authorization = token;
        return config;
    },
    error => {
        showError(error.message);
        return Promise.reject(error);
    });

axios.interceptors.response.use(undefined,
    error => {
        if (error.response && error.response.status && [401, 403].indexOf(error.response.status) !== -1)
            showError("Ошибка авторизации");
        else if (error.response && error.response.data && error.response.data.message)
            showError(error.response.data.message);
        else
            showError(error.message);
        return Promise.reject(error);
    });

export default new BackendService()