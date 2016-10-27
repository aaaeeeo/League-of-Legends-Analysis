REGISTER 'elephant-bird.jar';

%declare SEQFILE_LOADER 'com.twitter.elephantbird.pig.load.SequenceFileLoader';
%declare BYTE_CONVERTER 'com.twitter.elephantbird.pig.load.BytesWritable';
%declare INT_CONVERTER 'com.twitter.elephantbird.pig.util.IntWritableConverter';

A = LOAD '/inputs/thriftWeather/weather-2006' USING $SEQFILE_LOADER (
  '-c $INT_CONVERTER', '-c $BYTE_CONVERTER'
) AS (key: int, value: byte);


DEFINE WeatherThriftBytesToTuple com.twitter.elephantbird.pig.piggybank.ThriftBytesToTuple('WeatherSummary');

B = FOREACH A GENERATE WeatherThriftBytesToTuple(value);

C = ORDER B BY meanTemperature DESC;
D = LIMIT C 1;
DUMP D;
