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
 * Change page function
 * @param url
 */
function goToPage(url){
    document.location.href = url;
}

/*===== Функции работы с таблицами ==================================================================*/

/**
 * Очистить содержимое таблицы
 * @param tableId
 */
function clearElementData(elementId){
    var table = document.getElementById(elementId);
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }
}

// Paint table cells
function paintTableCells(tableId, cssClass){
    console.log('TEST');
    $("#"+tableId+",#"+tableId+" th,#"+tableId+" td, #"+tableId+" input").addClass(cssClass);
};


