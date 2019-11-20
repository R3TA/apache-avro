package com.demo.avro.app;

import org.apache.avro.file.CodecFactory;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApacheAvroDemo1Application {

	public static void main(String[] args) {
		//SpringApplication.run(ApacheAvroDemo1Application.class, args);
		/*
		// Concept 1: Using constructors directly generally offers better performance,
		// as builders create a copy of the datastructure before it is written.
		User user1 = new User();
		user1.setName("Alyssa");
		user1.setFavoriteNumber(256);
		// Leave favorite color null
		
		// Alternate constructor
		User user2 = new User("Ben", 7, "red");
		
		// Construct via builder
		// Concept 2: builders will automatically set any default values specified in 
		// the schema. Additionally, builders validate the data as it set, whereas objects 
		// constructed directly will not cause an error until the object is serialized
		
		User user3 = User.newBuilder()
				.setName("Charlie")
				.setFavoriteColor("blue")
				.setFavoriteNumber(null)
				.build();
		
		
		// Serialization process
		// We create a DatumWriter, which converts Java objects into an in-memory serialized format.
		// The SpecificDatumWriter class is used with generated classes and extracts the schema from the specified generated type.
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
		
		// Next we create a DataFileWriter, which writes the serialized records, as well as the schema, to the file specified in 
		// the dataFileWriter.create call. We write our users to the file via calls to the dataFileWriter.append method. 
		// When we are done writing, we close the data file.
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		try {
			dataFileWriter.create(user1.getSchema(), new File("users.avro"));
			dataFileWriter.append(user1);
			dataFileWriter.append(user2);
			dataFileWriter.append(user3);
			dataFileWriter.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		// Deserializing process(is very similar to serialization process)
		// We create a SpecificDatumReader, analogous to the SpecificDatumWriter we used in serialization, which converts in-memory 
		// serialized items into instances of our generated class, in this case User
		
		DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
		try {
			// We pass the DatumReader and the previously created File to a DataFileReader, analogous to the DataFileWriter, which reads 
			// both the schema used by the writer as well as the data from the file on disk. The data will be read using the writer's schema 
			// included in the file and the schema provided by the reader, in this case the User class. The writer's schema is needed to know 
			// the order in which fields were written, while the reader's schema is needed to know what fields are expected and how to fill in 
			// default values for fields added since the file was written. If there are differences between the two schemas, they are resolved 
			// according to the Schema Resolution specification
			
			
			@SuppressWarnings({ "resource", "unchecked", "rawtypes" })
			DataFileReader<User> dataFileReader = new DataFileReader(new File("users.avro"), userDatumReader);
			
			// Next we use the DataFileReader to iterate through the serialized Users and print the deserialized object to stdout. Note how we perform 
			// the iteration: we create a single User object which we store the current deserialized user in, and pass this record object to every call 
			// of dataFileReader.next. This is a performance optimization that allows the DataFileReader to reuse the same User object rather than allocating 
			// a new User for every iteration, which can be very expensive in terms of object allocation and garbage collection if we deserialize a large 
			// data file. While this technique is the standard way to iterate through a data file, it's also possible to use for (User user : dataFileReader) 
			// if performance is not a concern.
			
			
			User user = null;
			while(dataFileReader.hasNext()) {
			//for (User user: dataFileReader) {
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
