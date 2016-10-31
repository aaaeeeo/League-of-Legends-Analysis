ADD JAR /Users/LZM/Documents/workspace/weather/target/weather.jar;

CREATE EXTERNAL TABLE gsod2016
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.thrift.ThriftDeserializer' WITH SERDEPROPERTIES (
'serialization.class' = 'edu.uchicago.mpcs53013.weatherSummary.WeatherSummary', 'serialization.format' =
'org.apache.thrift.protocol.TBinaryProtocol') STORED AS SEQUENCEFILE
LOCATION '/tmp/inputs/thriftWeather';

SELECT * from gsod2016 ORDER BY meanTemperature DESC LIMIT 10;
