package su.systserv.smartBuoy.database;

/**
 * Все доступные для пользователя типы баз данных.
 * Перечисленные здесь типы баз данных должны быть реализуемы фабрикой DbConnectorFactory
 */
public enum DatabaseType {
    MY_SQL, MS_SQL
}
