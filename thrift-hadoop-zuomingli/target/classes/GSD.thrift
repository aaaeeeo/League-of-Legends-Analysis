namespace java edu.uchicago.mpcs53013.GSD

struct StationID {
	1:optional i32 STN;
	2:optional i32 WBAN;
}

struct Data {
  1:required StationID sid;
  2:optional string YEARMODA;
  3:optional double TEMP;
  4:optional double DEWP;
  5:optional double SLP;
  6:optional double TP;
  7:optional double VISIB;
  8:optional double WDSP;
  9:optional double MXSPD;
  10:optional double GUST;
  11:optional double MAX;
  12:optional double MIN;
  13:optional double PRCP;
  14:optional double SNDP;
  15:optional string FRSHTT; 
}