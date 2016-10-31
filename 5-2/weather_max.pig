REGISTER ./elephant-bird-core-4.15.jar;
REGISTER ./elephant-bird-pig-4.14.jar;
REGISTER ./elephant-bird-hadoop-compat-4.14.jar;
REGISTER ./libthrift-0.9.3.jar;
REGISTER ./weather.jar;

%declare SEQFILE_LOADER 'com.twitter.elephantbird.pig.load.SequenceFileLoader';
%declare BYTE_CONVERTER 'com.twitter.elephantbird.pig.util.BytesWritableConverter';
%declare INT_CONVERTER 'com.twitter.elephantbird.pig.util.IntWritableConverter';

A = LOAD '/tmp/inputs/thriftWeather/weather-2006' USING $SEQFILE_LOADER (
  '-c $INT_CONVERTER', '-c $BYTE_CONVERTER'
) AS (key: int, value: bytearray);


DEFINE WeatherThriftBytesToTuple com.twitter.elephantbird.pig.piggybank.ThriftBytesToTuple('edu.uchicago.mpcs53013.weatherSummary.WeatherSummary');

B = FOREACH A GENERATE WeatherThriftBytesToTuple(value);

C = ORDER B BY meanTemperature DESC;
D = LIMIT C 10;
DUMP D;
