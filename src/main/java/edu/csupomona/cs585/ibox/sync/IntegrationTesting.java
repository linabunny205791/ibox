package edu.csupomona.cs585.ibox.sync;
import static org.junit.Assert.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class IntegrationTesting {
	
	Drive DriveService;
	GoogleDriveFileSyncManager GoogleDrive;
	
	
	@Before
	public void setUp() throws Exception {
		initialGoogleServices();
		GoogleDrive = new GoogleDriveFileSyncManager(DriveService);
	}
	
	@After
	public void tearDown() throws Exception {
		GoogleDrive = null;
	}
	
	@Test

	public void initialGoogleServices() throws IOException {
	        HttpTransport httpTransport = new NetHttpTransport();
	        JsonFactory jsonFactory = new JacksonFactory();
	        try
	        {
	            GoogleCredential credential = new  GoogleCredential.Builder()
	              .setTransport(httpTransport)
	              .setJsonFactory(jsonFactory)
	              .setServiceAccountId("406929951341-ptecucbku97jb4o6okuae2qcc6emom8h@developer.gserviceaccount.com")
	              .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
	              .setServiceAccountPrivateKeyFromP12File(new java.io.File("A1-CS85-f4c3238ac46f.p12"))
	              .build();

	            DriveService = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName("ibox").build();

	        }
	        catch(GeneralSecurityException e)
	        {
	            e.printStackTrace();
	        }
	 }
	
	@Test
	public void testIntegration() throws IOException 
	{
		java.io.File localFile = new java.io.File("test.txt");
		localFile.createNewFile();
		GoogleDrive.addFile(localFile);
		assertNotNull(GoogleDrive.getFileId(localFile.getName()));
		GoogleDrive.updateFile(localFile);
		assertNotNull(GoogleDrive.getFileId(localFile.getName()));
		GoogleDrive.deleteFile(localFile);
		assertNull(GoogleDrive.getFileId(localFile.getName()));
	}


}
