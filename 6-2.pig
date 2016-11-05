NEW_DATA = LOAD '/inputs/flights_and_weather/part*' USING PigStorage(',')
   as (year: int, month:int, day:int, carrier, origin, dep_code, dest, dep_delay, arr_delay, meanTemperature, meanVisibility, meanWindSpeed, fog, rain, snow, hail, thunder, tornado);
OLD_VIEW = LOAD '/views/route_delay/part*' USING PigStorage(',')
   as (origin, dest, type, delay:double, count:long);

FAW_GROUP = GROUP NEW_DATA BY (origin, dest, fog, rain, snow, hail, thunder, tornado);
FAW_SP_SUM = FOREACH FAW_GROUP GENERATE FLATTEN(group) AS (origin, dest, fog, rain, snow, hail, thunder, tornado), SUM(FLIGHTS_AND_WEATHER.dep_delay) AS dep_delay_sum, COUNT(FLIGHTS_AND_WEATHER) as count;

SPLIT FAW_SP_SUM INTO FOG if fog==1, RAIN if rain==1, SNOW if snow==1, HAIL if hail==1, THUNDER if thunder==1, TORNADO if tornado==1, CLEAR if (fog==0 AND rain==0 AND snow==0 AND hail==0 AND thunder==0 AND tornado==0);
CLEAR_G = GROUP CLEAR BY (origin, dest);
CLEAR_S = FOREACH CLEAR_G GENERATE group, 'CLEAR' as type, SUM(CLEAR.dep_delay_sum) as delay, SUM(CLEAR.count) as count;
FOG_G = GROUP FOG BY (origin, dest);
FOG_S = FOREACH FOG_G GENERATE group, 'FOG' as type, SUM(FOG.dep_delay_sum) as delay, SUM(FOG.count) as count;
RAIN_G = GROUP RAIN BY (origin, dest);
RAIN_S = FOREACH RAIN_G GENERATE group, 'RAIN' as type, SUM(RAIN.dep_delay_sum) as delay, SUM(RAIN.count) as count;
SNOW_G = GROUP SNOW BY (origin, dest);
SNOW_S = FOREACH SNOW_G GENERATE group, 'SNOW' as type, SUM(SNOW.dep_delay_sum) as delay, SUM(SNOW.count) as count;
HAIL_G = GROUP HAIL BY (origin, dest);
HAIL_S = FOREACH HAIL_G GENERATE group, 'HAIL' as type, SUM(HAIL.dep_delay_sum) as delay, SUM(HAIL.count) as count;
THUNDER_G = GROUP THUNDER BY (origin, dest);
THUNDER_S = FOREACH THUNDER_G GENERATE group, 'THUNDER' as type, SUM(THUNDER.dep_delay_sum) as delay, SUM(THUNDER.count) as count;
TORNADO_G = GROUP TORNADO BY (origin, dest);
TORNADO_S = FOREACH TORNADO_G GENERATE group, 'TORNADO' as type, SUM(TORNADO.dep_delay_sum) as delay, SUM(TORNADO.count) as count;
NEW_VIEW = UNION CLEAR_S, FOG_S, RAIN_S, SNOW_S, HAIL_S, THUNDER_S, TORNADO_S;
NEW_VIEW = FOREACH NEW_VIEW GENERATE FLATTEN(group) AS (origin, dest), type, delay, count;

ROUTE_DELAY = UNION NEW_VIEW, OLD_VIEW;
ROUTE_DELAY_G = GROUP ROUTE_DELAY BY (origin, dest, type);
ROUTE_DELAY = FOREACH ROUTE_DELAY_G GENERATE FLATTEN(group) AS (origin, dest, type), SUM(ROUTE_DELAY_G.delay) as delay, SUM(ROUTE_DELAY_G.count) as count;

STORE ROUTE_DELAY into '/views/route_delay' Using PigStorage(',');

