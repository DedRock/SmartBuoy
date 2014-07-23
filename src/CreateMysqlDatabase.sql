CREATE TABLE `buoy` (
  `mmsi` int(10) unsigned NOT NULL,
  `simNumber` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT 'Номер SIM-карты буя (10 символов)',
  `curLatitude` int(9) unsigned DEFAULT NULL COMMENT 'Координаты: широта текущая (в 1/10_000 минут)',
  `curLongtitude` int(9) unsigned DEFAULT NULL COMMENT 'Координаты: долгота текущая (в 1/10_000 минут)',
  `batteryResource` smallint(5) unsigned DEFAULT NULL COMMENT 'Запас по времени работы батареи ',
  `batteryNeedToChange` bit(1) DEFAULT NULL COMMENT 'Флаг требования замены батареи ',
  `voltage` float DEFAULT NULL COMMENT 'Напряжение текущее ',
  `flushDuration` tinyint(3) unsigned DEFAULT NULL COMMENT 'световая сигнализация : длительность вспышек',
  `flashPause` tinyint(3) unsigned DEFAULT NULL COMMENT 'световая сигнализация : промежуток между вспышками',
  `flashNumberInSeries` tinyint(3) unsigned DEFAULT NULL COMMENT 'световая сигнализация : количество вспышек в серии',
  `flashSeriesInterval` tinyint(3) unsigned DEFAULT NULL COMMENT 'световая сигнализация : задержка между сериями',
  `flashBrightness` tinyint(3) unsigned DEFAULT NULL COMMENT 'световая сигнализация : яркость вспышек',
  `flashStartTime` int(10) unsigned DEFAULT NULL COMMENT 'начало серии вспышек по UTC',
  `buoyTypeId` int(10) unsigned NOT NULL COMMENT 'тип буя (1: Basic, 2: Meteo, 3: Ecolog)',
  `defaultLatitude` int(9) unsigned DEFAULT NULL COMMENT 'Координаты: широта заданная (в 1/10_000 минут)',
  `defailtLongtitude` int(9) unsigned DEFAULT NULL COMMENT 'Координаты: долгота заданная (в 1/10_000 минут)',
  `name` varchar(25) NOT NULL COMMENT 'Имя',
  PRIMARY KEY (`mmsi`) USING BTREE,
  KEY `buoyType_FK` (`buoyTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `buoytype` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `meteo_buoy` (
  `mmsi` int(10) unsigned NOT NULL,
  `airTemp` float(3,1) DEFAULT NULL,
  `airHumidity` tinyint(3) unsigned DEFAULT NULL,
  `windSpeed` float DEFAULT NULL,
  `windDirection` smallint(5) unsigned DEFAULT NULL,
  `waterTemp` float DEFAULT NULL,
  `waterFlowRate` float DEFAULT NULL,
  `waterFlowDirection` smallint(5) unsigned DEFAULT NULL,
  PRIMARY KEY (`mmsi`) USING BTREE,
  CONSTRAINT `FK_meteo_buoy_1` FOREIGN KEY (`mmsi`) REFERENCES `buoy` (`mmsi`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ecolog_buoy` (
  `mmsi` int(10) unsigned NOT NULL,
  PRIMARY KEY (`mmsi`) USING BTREE,
  CONSTRAINT `FK_ecolog_buoy_1` FOREIGN KEY (`mmsi`) REFERENCES `buoy` (`mmsi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;