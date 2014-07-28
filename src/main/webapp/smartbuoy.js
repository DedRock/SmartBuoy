/**
 * Существующие типы буёв в системе
 */
BuoyTypeEnum = {
    BASIC : {name: "BASIC", value: 1},
    METEO : {name: "METEO", value: 2},
    ECOLOG : {name: "ECOLOG", value: 3}
}
// Server Settings
SERVER_NAME = 'localhost';
SERVER_PORT = 80;

/**
 * Функция определения типа буя по его идентификатору
 * @param id
 * @returns {*}
 */
function getBuoyTypeEnumById(id){
    switch (id){
        case 2 :
            return BuoyTypeEnum.METEO;
            break;

        case 3 :
            return BuoyTypeEnum.ECOLOG;
            break;

        default :
            return BuoyTypeEnum.BASIC;
    }
}
/**
 * Перейти на указанную страницу
 * @param url
 */
function goToPage(url){
    document.location.href = url;
}


/**
 * Выполнить ajax-запрос
 * @param url - адрес обращения
 * @param doneMethod - название метода в случае получения корректного ответа
 * @param errorMethod - название метода в случае получения ошибки
 */
function execAjaxQuery(url, doneMethod, errorMethod){
    $.get(url)
        .done(doneMethod)
        .error(errorMethod);
}

/**
 * Функция отображение ошибки ajax-запроса
 * @param errorAnswer
 */
function showAjaxError(errorAnswer){
    switch(errorAnswer.status){
        case 1000:
            alert("Ошибка клиентского ajax-запроса к серверу");
            break;
        case 1001:
            alert("Ошибка сервера: не удаётся подключитсья к базе данных");
            break;
        case 1002:
            alert("Ошибка сервера при выполнениии ajax-запроса");
            break;
    }
    // Сам текст ошибки можно посомтреть в логе браузера, либо в:
    // errorAnswer.responseText
}


/**
 * Очистить содержимое элементы
 * @param tableId
 */
function clearElementData(elementId){
    var table = document.getElementById(elementId);
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }
}
/*===== Функции работы с таблицами ==================================================================*/
// Ко всем ячейкам таблицы применить класс CSS
function paintTableCells(tableId, cssClass){
    console.log('TEST');
    $("#"+tableId+",#"+tableId+" th,#"+tableId+" td, #"+tableId+" input").addClass(cssClass);
};

/**
 * Добавить в таблицу строку, объединяющую несколько столбцов
 * @param tableId - id таблицы
 * @param caption - текст в строке
 * @param colSpan - number of columns
 */
function addGroupRow(tableId, caption, colSpan){
    // new Row
    var row = document.createElement('TR');
    // Name of parameter
    var tdCaption = document.createElement('TD');
    tdCaption.appendChild(document.createTextNode(caption));
    tdCaption.colSpan = colSpan;
    // Fill row
    row.appendChild(tdCaption);
    var tbl = document.getElementById(tableId);
    // Add row to table
    tbl.appendChild(row);
    //paintTableCells(tableId, borderTableCell);
}


