package edu.uchicago.mpcs53013.GSD;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.io.serializer.Deserializer;
import org.apache.hadoop.io.serializer.Serialization;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
public class DataSerialization implements Serialization<Data> {

	class DataDeserializer implements Deserializer<Data> {
		  private TProtocol protocol;

		@Override
		public void close() throws IOException {
		}

		@Override
		public Data deserialize(Data data) throws IOException {
			if(data == null) {
				data = new Data();
			}
			try {
				data.read(protocol);
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}

		@Override
		public void open(InputStream is) throws IOException {
			protocol = new TJSONProtocol(new TIOStreamTransport(is));
		}
		
	}
	
	class DataSerializer implements Serializer<Data> {
		  private TProtocol protocol;

		@Override
		public void close() throws IOException {
		}

		@Override
		public void open(OutputStream os) throws IOException {
			protocol = new TJSONProtocol(new TIOStreamTransport(os));
		}

		@Override
		public void serialize(Data data) throws IOException {
			try {
				data.write(protocol);
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	@Override
	public boolean accept(Class<?> clazz) {
		return clazz.equals(Data.class);
	}

	@Override
	public Deserializer<Data> getDeserializer(Class<Data> clazz) {
		return new DataDeserializer();
	}

	@Override
	public Serializer<Data> getSerializer(Class<Data> clazz) {
		return new DataSerializer();
	}

}
