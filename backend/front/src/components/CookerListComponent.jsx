import React, { Component } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrash, faEdit, faPlus } from '@fortawesome/fontawesome-free-solid'
import BackendService from "../services/BackendService";
import Alert from "./Alert";
import PaginationComponent from "./PaginationComponent";

class CookerListComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            message: undefined,
            cookers: [],
            selected_cookers: [],
            show_alert: false,
            checkedItems: [],
            hidden: false,
            page: 1,
            limit: 2,
            totalCount: 0
        };

        this.refreshCookers = this.refreshCookers.bind(this);
        this.updateCookerClicked = this.updateCookerClicked.bind(this);
        this.addCookerClicked = this.addCookerClicked.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.closeAlert = this.closeAlert.bind(this);
        this.handleCheckChange = this.handleCheckChange.bind(this);
        this.handleGroupCheckChange = this.handleGroupCheckChange.bind(this);
        this.setChecked = this.setChecked.bind(this);
        this.deleteCookersClicked = this.deleteCookersClicked.bind(this);

        this.onPageChanged = this.onPageChanged.bind(this);
    }

    onPageChanged(cp) {
        this.refreshCookers(cp - 1)
    }

    // Ставит или снимает отметку всех чек боксов.
    // В заголовок таблицы мы вставим чек бокс, который позволит нам устанавливать или сбрасывать все
    // чек боксы в строках таблицы с помощью этой функции. Мы не можем просто поменять значение в
    // поле состояния checkedItems. Поэтому делаем копию этого поля и заменяем его на эту
    // копию с помощью функции this.setState.
    setChecked(v)
    {
        let checkedCopy = Array(this.state.cookers.length).fill(v);
        this.setState( { checkedItems : checkedCopy })
    }

    // Здесь используется принцип контролируемого ввода.
    // При изменении состояния чек бокса вызывается эта функция, которая записывает состояние чек бокса
    // в поле состояния checkedItems. Здесь также делаем копию, вносим в нее изменения и сохраняем обратно
    // в состояние с помощью setState.
    handleCheckChange(e)
    {
        const idx = e.target.name;
        const isChecked = e.target.checked;

        let checkedCopy = [...this.state.checkedItems];
        checkedCopy[idx] = isChecked;
        this.setState({ checkedItems: checkedCopy });
    }

    // Обработчик чек бокса в заголовке таблицы. Он устанавливает или сбрасывает все чек боксы
    // в строках таблицы.
    handleGroupCheckChange(e)
    {
        const isChecked = e.target.checked;
        this.setChecked(isChecked);
    }

    // Эта функция вызывается по кнопке Удалить. Она с помощью функции map, которая
    // аналогична циклу, пробегает по всем странам и, одновременно, по всем элементам
    // checkedItems для того, чтобы определить какие страны отмечены для удаления. Эти
    // страны помещаются в массив x. Если массив оказывается пустым, то на этом все заканчивается.
    deleteCookersClicked() {
        let x = [];
        this.state.cookers.map ((t, idx) => {
            if (this.state.checkedItems[idx]) {
                x.push(t)
            }
            return 0
        });
        if (x.length > 0) {
            let msg;
            if (x.length > 1)
            {
                msg = "Пожалуйста подтвердите удаление " + x.length + " поваров";
            }
            else
            {
                msg = "Пожалуйста подтвердите удаление повара " + x[0].name;
            }
            this.setState({ show_alert: true, selected_cookers: x, message: msg });
        }
    }

    // Вызывает REST сервис для удаления списка поваров и, в случае успешного завершения этой операции,
    // обновляет список оставшихся поваров на экране.
    onDelete()
    {
        BackendService.deleteCookers(this.state.selected_cookers)
            .then( () => this.refreshCookers())
            .catch(()=>{})
    }

    // Закрывает модальное окно Alert.
    closeAlert()
    {
        this.setState({ show_alert : false })
    }

    // Функция обновления списка поваров считывает его с помощью запроса к REST сервису и
    // помещает в поле состояния cookers. В случае ошибки, устанавливает флаг hidden,
    // который используется в методе render для того, чтобы убрать с экрана изображение таблицы.
    // В параметрах функции передается номер текущей страницы, считая от нуля.
    refreshCookers(cp) {
        console.log("cp2", this.state.page);
        BackendService.retrieveAllCookers(cp, this.state.limit)
            .then(
                resp => {
                    this.setState( { cookers: resp.data.content,
                        totalCount: resp.data.totalElements, page: cp, hidden: false });
                }
            )
            .catch(()=> { this.setState({ totalCount: 0, hidden: true })})
            .finally(()=> this.setChecked(false))
    }

    // Метод вызывается при загрузке страницы.
    // Здесь загружаются данные, необходимые для ее прорисовки.
    componentDidMount() {
        this.refreshCookers(0)
    }

    // Обработчик кнопки для перехода к странице редактирования повара.
    // Такая кнопка есть в каждой строчке таблицы поваров.
    updateCookerClicked(id) {
        this.props.history.push(`/cookers/${id}`)
    }

    // Кнопка для добавления повара.
    // При добавлении повара мы указываем в URL индекс -1.
    addCookerClicked() {
        this.props.history.push(`/cookers/-1`)
    }

    render() {
        if (this.state.hidden) // Если hidden равно false, возвращаем null ничего не рисуя.
            return null;
        return ( // Иначе выводим заголовок и две кнопки для добавления страны и удаления стран.
            <div className="m-4">
                <div className="row my-2 mr-0">
                    <h3>Повары</h3>
                    <button className="btn btn-outline-secondary ml-auto"
                            onClick={this.addCookerClicked}><FontAwesomeIcon icon={faPlus}/>{' '}Добавить</button>
                    <button className="btn btn-outline-secondary ml-2"
                            onClick={this.deleteCookersClicked}><FontAwesomeIcon icon={faTrash}/>{' '}Удалить</button>
                </div>
                <div className="row my-2 mr-0">
                    <PaginationComponent
                        totalRecords={this.state.totalCount}
                        pageLimit={this.state.limit}
                        pageNeighbours={1}
                        onPageChanged={this.onPageChanged} />
                    <table className="table table-sm">
                        <thead className="thead-light">
                        <tr>
                            <th>Имя</th>
                            <th>
                                <div className="btn-toolbar pb-1">
                                    <div className="btn-group  ml-auto">
                                        <input type="checkbox"  onChange={this.handleGroupCheckChange}/>
                                    </div>
                                </div>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            this.state.cookers && this.state.cookers.map((cooker, index) =>
                                <tr key={cooker.id}>
                                    <td>{cooker.name}</td>
                                    <td>
                                        <div className="btn-toolbar">
                                            <div className="btn-group  ml-auto">
                                                <button className="btn btn-outline-secondary btn-sm btn-toolbar"
                                                        onClick={() => this.updateCookerClicked(cooker.id)}>
                                                    <FontAwesomeIcon icon={faEdit} fixedWidth/></button>
                                            </div>
                                            <div className="btn-group  ml-2 mt-1">
                                                <input type="checkbox" name={index}
                                                       checked={this.state.checkedItems.length > index ?  this.state.checkedItems[index] : false}
                                                       onChange={this.handleCheckChange}/>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            )
                        }
                        </tbody>
                    </table>
                </div>
                <Alert
                    title="Удаление"
                    message={this.state.message}
                    ok={this.onDelete}
                    close={this.closeAlert}
                    modal={this.state.show_alert}
                    cancelButton={true}
                />
            </div>
        )
    }
}

export default CookerListComponent;