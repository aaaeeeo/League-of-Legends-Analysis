FLIGHTS_AND_WEATHER = LOAD '/inputs/flights_and_weather' USING PigStorage(',')
as (year:int, month:int, day:int,
  carrier, origin, dep_code, dest, dep_delay:int, arr_delay:int, meanTemperature:double, meanVisibility: double, 
    meanWindSpeed:double, fog:int, rain:int, snow:int,  hail:int, thunder:int, tornado:int);

FLIGHTS_AND_WEATHER_DELAYS = FOREACH FLIGHTS_AND_WEATHER GENERATE
  origin, dest, carrier,
  fog, (fog == 1 ? dep_delay : 0) AS fog_delay, 
  rain, (rain == 1 ? dep_delay : 0) AS rain_delay,
  snow, (snow == 1 ? dep_delay : 0) AS snow_delay,
  hail, (hail == 1 ? dep_delay : 0) AS hail_delay,
  thunder, (thunder == 1 ? dep_delay : 0) AS thunder_delay,
  tornado, (tornado == 1 ? dep_delay : 0) AS tornado_delay,
  (fog == 1 OR rain == 1 OR snow == 1 OR hail == 1 OR thunder == 1 OR tornado == 1 ? 0 : 1) as clear,
  (fog == 1 OR rain == 1 OR snow == 1 OR hail == 1 OR thunder == 1 OR tornado == 1 ? 0 : dep_delay) as clear_delay;

DELAYS_BY_ROUTE = GROUP FLIGHTS_AND_WEATHER_DELAYS BY (origin, dest, carrier);
SUMMED_DELAYS_BY_ROUTE = FOREACH DELAYS_BY_ROUTE 
  GENERATE CONCAT(group.origin, group.dest, '_', group.carrier) AS route,
           group.carrier AS carrier,
           SUM($1.fog) AS fog_flights, SUM($1.fog_delay) AS fog_delays,
           SUM($1.rain) AS rain_flights, SUM($1.rain_delay) AS rain_delays,
           SUM($1.snow) AS snow_flights, SUM($1.snow_delay) AS snow_delays,
           SUM($1.hail) AS hail_flights, SUM($1.hail_delay) AS hail_delays,
           SUM($1.thunder) AS thunder_flights, SUM($1.thunder_delay) AS thunder_delays,
           SUM($1.tornado) AS tornado_flights, SUM($1.tornado_delay) AS tornado_delays,
           SUM($1.clear) AS clear_flights, SUM($1.clear_delay) AS clear_delays;


STORE SUMMED_DELAYS_BY_ROUTE INTO 'hbase://weather_delays_by_route_carrier'
  USING org.apache.pig.backend.hadoop.hbase.HBaseStorage(
    'info:carrier, delay:fog_flights, delay:fog_delays, delay:rain_flights, delay:rain_delays, delay:snow_flights, delay:snow_delays, delay:hail_flights, delay:hail_delays, delay:thunder_flights, delay:thunder_delays, delay:tornado_flights, delay:tornado_delays, delay:clear_flights, delay:clear_delays');