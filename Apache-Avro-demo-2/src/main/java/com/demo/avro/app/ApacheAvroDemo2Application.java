package com.demo.avro.app;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApacheAvroDemo2Application {

	public static void main(String[] args) {
		//SpringApplication.run(ApacheAvroDemo2Application.class, args);
		
		String schemaRoot = System.getProperty("user.dir") + "/src/main/resources/avro/";
	    System.out.println(schemaRoot);
	    //Schema schema = new Parser().parse(new File(schemaRoot + "user.avsc"));
		try {
			// First step: We use a Parser to read our schema definition and create a Schema object
			Schema schema = new Schema.Parser().parse(new File(schemaRoot + "user.avsc"));
			GenericRecord user1  = new GenericData.Record(schema);
			GenericRecord user2 = new GenericData.Record(schema);
			
			user1.put("name", "Alyssa");
			user1.put("favorite_number", 256);
			// Leave favorite color null

			user2.put("name", "Ben");
			user2.put("favorite_number", 7);
			user2.put("favorite_color", "Red");
			
			// Serialization process
			// First we'll serialize our users to a data file on disk.
			File file = new File("users.avro");
			
			// Converts Java Objects into an in-memory serialized format. Since we are not using code generation,
			// we create a GenericDatumWriter. It requires the schema both to determine how to write the GenericRecords 
			// and to verify that all non-nullable fields are present.
			DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
			
			// As in the code generation example, we also create a DataFileWriter, which writes the serialized records, 
			// as well as the schema, to the file specified in the dataFileWriter.create call. We write our users to the 
			// file via calls to the dataFileWriter.append method. When we are done writing, we close the data file.
			DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);

			dataFileWriter.create(schema,file);
			dataFileWriter.append(user1);
			dataFileWriter.append(user2);
			dataFileWriter.close();
			
			
			// Deserializizing
			// Finally, we'll deserialize the data file we just created.
			DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
			DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
			GenericRecord user = null;
			while(dataFileReader.hasNext()) {
			// Reuse user object by passing it to next(). This saves us from
			// allocating and garbage collecting many objects for files with
			// many items.
				user = dataFileReader.next(user);
				System.out.println(user);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	

}
