REGISTER ./piggybank.jar;
REGISTER ./elephant-bird-core-4.14.jar;
REGISTER ./elephant-bird-pig-4.14.jar;
REGISTER ./elephant-bird-hadoop-compat-4.14.jar;
REGISTER ./libthrift-0.9.0.jar;
REGISTER ./thrift_structure.jar;
REGISTER ./hadoop-lzo-0.4.15.jar;

-- step 1
STATION_CODE_LINES = LOAD '/inputs/weathergeo/station_codes.txt' as line;
STATIONS_FILTERED = FILTER STATION_CODE_LINES by SUBSTRING($0, 10, 11) eq ' ' and SUBSTRING($0, 7, 8) neq ' ';
STATION_CODES = FOREACH STATIONS_FILTERED GENERATE SUBSTRING($0, 0, 6) as code, SUBSTRING($0, 7, 10) as name;

RAW_ONTIME_DATA = LOAD '/inputs/airline/On_Time_On_Time_Performance_*' USING org.apache.pig.piggybank.storage.CSVExcelStorage(',', 'NO_MULTILINE', 'NOCHANGE', 'SKIP_INPUT_HEADER'); 

ONTIME_DATA = FOREACH RAW_ONTIME_DATA GENERATE $0 AS year, $2 AS month, $3 as day, $6 AS carrier, $14 AS origin, $23 AS dest, $29 AS sched_dep, $31 as dep_delay, $42 as arr_delay;
    
ONTIME_DATA_FILTERED = FILTER ONTIME_DATA BY (origin neq '') AND (dest neq '') AND (dep_delay neq '') AND (arr_delay neq '');
DEPARTURES_WITH_WEATHER_STATIONS = JOIN ONTIME_DATA_FILTERED by origin, STATION_CODES by name PARALLEL 5;
DEPARTURES_STATIONS = FOREACH DEPARTURES_WITH_WEATHER_STATIONS GENERATE code as station, year, month, day, carrier, origin, dest, dep_delay, arr_delay;

DEFINE ThriftStorage com.twitter.elephantbird.pig.store.LzoThriftBlockPigStorage('edu.uchicago.mpcs53013.flight.Flight');
STORE DEPARTURES_STATIONS INTO '/inputs/flight_stations_thrift/' USING ThriftStorage();

-- step 2
DEFINE ThriftLoader com.twitter.elephantbird.pig.load.LzoThriftBlockPigLoader('edu.uchicago.mpcs53013.flight.Flight');
DEPARTURE_STATIONS = LOAD '/inputs/flight_stations_thrift/part*' USING ThriftLoader();

DEFINE WSThriftBytesToTuple com.twitter.elephantbird.pig.piggybank.ThriftBytesToTuple('edu.uchicago.mpcs53013.weatherSummary.WeatherSummary');
RAW_DATA = LOAD '/inputs/thriftWeather' USING org.apache.pig.piggybank.storage.SequenceFileLoader() as (key:long, value: bytearray);
WEATHER_SUMMARY = FOREACH RAW_DATA GENERATE FLATTEN(WSThriftBytesToTuple(value));
FLIGHTS_AND_WEATHER_RAW = JOIN WEATHER_SUMMARY by (WeatherSummary::year, WeatherSummary::month, WeatherSummary::day, WeatherSummary::station), DEPARTURE_STATIONS by (year, month, day, dep_code);
FLIGHTS_AND_WEATHER = FOREACH FLIGHTS_AND_WEATHER_RAW GENERATE WeatherSummary::year, WeatherSummary::month, WeatherSummary::day,
  carrier, origin, dep_code, dest, dep_delay, arr_delay, meanTemperature, meanVisibility, meanWindSpeed, fog, rain, snow,
  hail, thunder, tornado;
STORE FLIGHTS_AND_WEATHER into '/inputs/flights_and_weather' Using PigStorage(',');


